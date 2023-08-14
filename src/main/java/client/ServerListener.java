package client;

import client.animations.Animation;
import client.animations.AnimationFactory;
import client.animations.AnimationList;
import game.Player;
import protocol.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ServerListener class listens to server data and updates player information accordingly.
 */
public class ServerListener implements Runnable {

    private DatagramSocket socket;
    private DatagramPacket packet;
    private ConcurrentHashMap<String, Player> players;

    private AnimationList animations;

    /**
     * Constructs a new ServerListener instance.
     *
     * @param players The ConcurrentHashMap containing player information.
     * @param socket  The DatagramSocket for communication.
     */
    public ServerListener(ConcurrentHashMap<String, Player> players, DatagramSocket socket, List<Animation> animations) {
        this.socket = socket;
        this.players = players;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.animations = (AnimationList) animations;
    }

    /**
     * Continuously listens for server data and updates player information accordingly.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Data data;
            try {
                socket.receive(packet);
                data = ServerDataFactory.createData(packet.getData());
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }

            if (data.getDataType() == DataType.STATEDATA){

                processStatePacket((StateData) data);
            }

            else if (data.getDataType() == DataType.ANIMATIONDATA){
                System.out.println(data.getDataType());
                Optional<Animation> builderOptional = AnimationFactory.createAnimation(players, (AnimationData) data);
                builderOptional.ifPresent(b -> animations.add(b));
            }

            else {
                System.out.println(data.getDataType());
            }

        }
        System.out.println("Server listener shutting down");
    }

    private void processStatePacket(StateData data) {
        if (players.size() < data.getPlayerCount()) {
            addNewPlayers(data);
        } else if (players.size() > data.getPlayerCount()) {
            removeDisconnectedPlayers(data);
        }

        for (int i = 0; i < data.getPlayerCount(); i++) {
            Player player = players.get(data.getPlayerNames()[i]);
            if (player != null) {
                player.setX(data.getX()[i]);
                player.setY(data.getY()[i]);
                player.setCurrHp(data.getCurrHps()[i]);
            }
        }
    }

    private void addNewPlayers(StateData data) {
        for (int i = 0; i < data.getPlayerCount(); i++) {
            players.putIfAbsent(data.getPlayerNames()[i], new Player(data.getPlayerNames()[i], data.getX()[i], data.getY()[i]));
        }
    }

    private void removeDisconnectedPlayers(StateData data) {
        Iterator<String> it = players.keys().asIterator();

        while (it.hasNext()) {
            String player = it.next();
            if (!Arrays.asList(data.getPlayerNames()).contains(player)) {
                players.remove(player);
            }
        }
    }
}
