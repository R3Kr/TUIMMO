package client;

import game.Player;
import protocol.ServerData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ServerListener class listens to server data and updates player information accordingly.
 */
public class ServerListener implements Runnable {

    private DatagramSocket socket;
    private DatagramPacket packet;
    private ConcurrentHashMap<String, Player> players;

    /**
     * Constructs a new ServerListener instance.
     *
     * @param players The ConcurrentHashMap containing player information.
     * @param socket  The DatagramSocket for communication.
     */
    public ServerListener(ConcurrentHashMap<String, Player> players, DatagramSocket socket) {
        this.socket = socket;
        this.players = players;
        this.packet = new DatagramPacket(new byte[1024], 1024);
    }

    /**
     * Continuously listens for server data and updates player information accordingly.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ServerData data;
            try {
                socket.receive(packet);
                data = new ServerData(packet.getData());
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            processServerPacket(data);
        }
        System.out.println("Server listener shutting down");
    }

    private void processServerPacket(ServerData data) {
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

    private void addNewPlayers(ServerData data) {
        for (int i = 0; i < data.getPlayerCount(); i++) {
            players.putIfAbsent(data.getPlayerNames()[i], new Player(data.getPlayerNames()[i], data.getX()[i], data.getY()[i]));
        }
    }

    private void removeDisconnectedPlayers(ServerData data) {
        Iterator<String> it = players.keys().asIterator();

        while (it.hasNext()) {
            String player = it.next();
            if (!Arrays.asList(data.getPlayerNames()).contains(player)) {
                players.remove(player);
            }
        }
    }
}
