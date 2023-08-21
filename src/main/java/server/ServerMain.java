package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import game.Action;
import game.World;
import game.components.NPC;
import game.components.Player;
import game.effects.Effect;
import game.systems.ClientHandlingSystem;
import game.systems.EffectSystem;
import game.systems.NPCStateSystem;
import game.systems.PlayerConnecterSystem;
import protocol.KryoFactory;
import protocol.data.AnimationData;
import protocol.data.DisconnectPlayer;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerMain {
    private Server server;
    private World world;

    private Queue<Action> actionDataQueue;
    private Queue<String> playersToConnect;

    private Queue<String> playersToDisconnect;

    private Queue<AnimationData> animationDataQueue;

    private Queue<Effect> effectQueue;

    public ServerMain(Server server) {
        this.server = server;
        world = new World();
        actionDataQueue = new ConcurrentLinkedQueue<>();
        playersToConnect = new ConcurrentLinkedQueue<>();
        playersToDisconnect = new ConcurrentLinkedQueue<>();
        animationDataQueue = new ConcurrentLinkedQueue<>();
        effectQueue = new ConcurrentLinkedQueue<>();

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();
        Log.set(2);
        KryoFactory.init(server.getKryo());
        server.start();
        server.bind(6969, 6970);

        new ServerMain(server).init();


    }

    private void init() throws InterruptedException {

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                super.connected(connection);
                connection.addListener(new ClientListener(actionDataQueue, playersToConnect, playersToDisconnect, animationDataQueue, effectQueue, world));
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

            }

        });

        Runnable broadcastState = () -> {
            world.query(Player.class, (p) -> true).forEach(p -> server.sendToAllUDP(p));
            world.query(NPC.class, n -> true).forEach(n -> server.sendToAllUDP(n));
        };

        world.addNPC("albert", 20, 20);
        world.addNPC("donkey", 21, 20);
        world.addNPC("ernst", 22, 20);
        world.addNPC("tu madre", 23, 20);
        world.addNPC("bill clinton", 24, 20);
        world.addNPC("bill wad", 24, 20);
        world.addNPC("bill clintaon", 24, 20);
        world.addNPC("bill ssadasdasd", 24, 20);
        world.addNPC("bill wdwdw", 24, 20);
        world.addNPC("bill clinqweqweqweton", 24, 20);
        world.addNPC("bill qwew", 24, 20);
        world.addNPC("bill clinqweqwwwweqweton", 24, 20);
        world.addNPC("bill clinqweqqqqweqweton", 24, 20);
        world.addNPC("bill clinqffffweqweqweton", 24, 20);

        world.addSystem(new ClientHandlingSystem(actionDataQueue, animationDataQueue, broadcastState, ((integer, o) -> server.sendToAllExceptUDP(integer, o))))
                .addSystem(new PlayerConnecterSystem(playersToConnect, playersToDisconnect,
                        s -> world.addPlayer(s, 10, 10),
                        s -> {
                            world.remove(s);
                            server.sendToAllUDP(new DisconnectPlayer(s));
                        },
                        broadcastState))
                .addSystem(new NPCStateSystem(() -> world.query(NPC.class, n -> true).forEach(n -> server.sendToAllUDP(n)), () -> world.query(NPC.class, n -> true)))
                .addSystem(new EffectSystem(effectQueue));

        // .addSystem(new StateBroadcasterSystem(stringPositionMap -> server.sendToAllUDP(stringPositionMap), () -> world.getPositions()));

        run();

    }

    private void run() throws InterruptedException {
        while (true) {
            world.tick();
//            world.query(Player.class, position -> true).forEach(Player::print);
//            System.out.println("-------------------------");
            Thread.sleep(20);
        }
    }
}
