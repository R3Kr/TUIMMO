package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import game.actions.Action;
import game.World;
import game.components.GameObject;
import game.components.NPC;
import game.components.Player;
import game.effects.Effect;
import game.systems.*;
import protocol.KryoFactory;
import protocol.data.AnimationData;
import protocol.data.ConnectPlayer;
import protocol.data.DisconnectGameobject;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public class ServerMain {
    private Server server;
    private World world;

    private Queue<Action> actionDataQueue;
    private Queue<ConnectPlayer> playersToConnect;

    private Queue<String> playersToDisconnect;

    private Queue<AnimationData> animationDataQueue;

    private Queue<Effect> effectQueue;
    private Queue<DisconnectGameobject> removalQueue;
    private Queue<GameObject> objectsToUpdate;

    public ServerMain(Server server) {
        this.server = server;
        world = new World();
        actionDataQueue = new ConcurrentLinkedQueue<>();
        playersToConnect = new ConcurrentLinkedQueue<>();
        playersToDisconnect = new ConcurrentLinkedQueue<>();
        animationDataQueue = new ConcurrentLinkedQueue<>();
        effectQueue = new ConcurrentLinkedQueue<>();
        removalQueue = new ConcurrentLinkedQueue<>();
        objectsToUpdate = new ConcurrentLinkedQueue<>();

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = new Server();

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
                connection.addListener(new ThreadedListener(new ClientListener(actionDataQueue, playersToConnect, playersToDisconnect, animationDataQueue, effectQueue, world)));
            }

            @Override
            public void received(Connection connection, Object object) {
                super.received(connection, object);

            }

        });


        Consumer<ConnectPlayer> broadcastStateOnLogin = (connectPlayer) -> {
            world.query(p -> !p.getName().equals(connectPlayer.player)).forEach(p -> server.sendToTCP(connectPlayer.connectionId, p));
            world.queryPlayer(connectPlayer.player).ifPresent(p -> server.sendToAllExceptTCP(connectPlayer.connectionId, p));

        };



        Consumer<DisconnectGameobject> removeObject = o -> {
          world.remove(o.player);
          server.sendToAllTCP(o);
        };

        world.addNPC("albert", 40, 0);
        world.addNPC("donkey", 71, 20);
        world.addNPC("ernst", 62, 0);
        world.addNPC("tu madre", 53, 20);
        world.addNPC("bill clinton", 44, 0);
        world.addNPC("bill wad", 34, 20);
        world.addNPC("bill clintaon", 24, 0);
        world.addNPC("bill ssadasdasd", 14, 20);
        world.addNPC("bill wdwdw", 4, 0);
        world.addNPC("bill clinqweqweqweton", 74, 10);
        world.addNPC("bill qwew", 64, 20);
        world.addNPC("bill clinqweqwwwweqweton", 54, 10);
        world.addNPC("bill clinqweqqqqweqweton", 44, 10);
        world.addNPC("bill clinqffffweqweqweton", 34, 0);

        world.addSystem(new ClientHandlingSystem(actionDataQueue, animationDataQueue, objectsToUpdate, stateData -> server.sendToAllUDP(stateData), ((integer, o) -> server.sendToAllExceptUDP(integer, o))))
                .addSystem(new PlayerConnecterSystem(playersToConnect, playersToDisconnect,
                        s -> world.addPlayer(s, 10, 10),
                        s -> {
                            world.remove(s);
                            server.sendToAllUDP(new DisconnectGameobject(s));
                        },
                        broadcastStateOnLogin))
                .addSystem(new NPCStateSystem(s -> server.sendToAllUDP(s), () -> world.query(NPC.class, n -> true), () -> world.query(Player.class, p -> true).findFirst(), removalQueue, actionDataQueue, animationDataQueue, objectsToUpdate, o -> world.createAttacks(o)))
                .addSystem(new EffectSystem(effectQueue))
                        .addSystem(new ObjectRemovalSystem(removalQueue, removeObject));

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
