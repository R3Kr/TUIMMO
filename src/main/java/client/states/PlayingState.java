package client.states;

import client.animations.Animation;
import client.animations.AnimationList;
import client.animations.AttackAnimation;
import client.animations.CoolAnimation;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import game.Attack;
import game.World;
import game.components.Player;
import game.systems.InputHandlingSystem;
import game.systems.RenderSystem;
import game.systems.StateRecieverSystem;
import protocol.data.AnimationData;
import protocol.data.DisconnectPlayer;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class PlayingState implements ClientState {


    private Screen screen;
    private Client client;
    private List<Animation> animations = new AnimationList();
    private Supplier<ClientState> onNext;

    private Queue<KeyStroke> keyStrokeQueue;

    private Queue<Player> playersToUpdate;

    private World world = new World();
    private Player player;


    public PlayingState(Screen screen, Client client, Supplier<String> playerName, String address) throws IOException {

        this.client = client;
        this.screen = screen;
        keyStrokeQueue = new LinkedList<>();
        playersToUpdate = new LinkedList<>();

        init(playerName.get());





    }
    private void init(String playerName) throws IOException {
        client.addListener(new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Player){
                    playersToUpdate.offer((Player) object);
                } else if (object instanceof DisconnectPlayer) {
                    world.remove(((DisconnectPlayer) object).player);
                }
                else if (object instanceof AnimationData){
                    AnimationData ad = (AnimationData) object;
                    Player p = world.query(ad.player).get();
                    switch (ad.type){
                        case ATTACK -> animations.add(new AttackAnimation(p));
                        case COOL -> animations.add(new CoolAnimation(p));
                    }

                }
                StringBuilder stringBuilder = new StringBuilder("Players: ");
                world.query(Player.class, player -> true).forEach(p -> stringBuilder.append("\n"+p.toString()));
                Log.info(stringBuilder.toString());
            }
        });

        player = world.add(playerName, 10, 10);



        world.addSystem(new InputHandlingSystem(keyStrokeQueue, animations, player, playerName, client::sendUDP, () -> world.createAttacks(player)))
                .addSystem(new RenderSystem(screen, () -> world.query(Player.class, p -> true), player, animations))
                .addSystem(new StateRecieverSystem(playersToUpdate, world.getPlayers()));

    }

    @Override
    public ClientState tick() throws IOException {

        KeyStroke keyStroke = screen.pollInput();

        // Handle input
        if (keyStroke != null) {
            if (keyStroke.getKeyType() == KeyType.Escape){
                client.sendTCP(new DisconnectPlayer(player.getName()));
                return onNext.get();
            }
            keyStrokeQueue.offer(keyStroke);

        }
        world.tick();



        //threadPool.shutdownNow();
        //screen.stopScreen();
        return this;
    }

    @Override
    public void shutDown() {

    }

    @Override
    public void setOnNext(Supplier<ClientState> onNext) {
        this.onNext = onNext;
    }



}
