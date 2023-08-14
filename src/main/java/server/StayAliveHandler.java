package server;

import game.Player;
import protocol.StayAliveData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * The StayAliveHandler class manages handling stay-alive messages from clients and disconnects inactive clients and players.
 */
public class StayAliveHandler implements Runnable {

    private GameState gameState;
    private DatagramSocket socket;

    private Clients clients;

    private DatagramPacket packet;

    private Map<SocketAddress, Long> clientTimeStamps;
    //private Map<String, Long> playerTimeStamps;

    /**
     * Constructs a StayAliveHandler object with the specified game state and client list.
     *
     * @param gameState The game state containing player information.
     * @param clients   The list of client SocketAddresses.
     * @throws SocketException if there is an issue with the DatagramSocket.
     */
    public StayAliveHandler(GameState gameState, Clients clients) throws SocketException {
        this.gameState = gameState;
        this.socket = new DatagramSocket(6970);
        this.clients = clients;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clientTimeStamps = new HashMap<>();
        //this.playerTimeStamps = new HashMap<>();
    }

    /**
     * Continuously runs the stay-alive handling process, managing client and player disconnections.
     */
    @Override
    public void run() {
        while (true) {
            System.out.println(clientTimeStamps);
            //System.out.println(playerTimeStamps);

            // Ensure clientTimeStamps map is up-to-date
            clients.forEach(client -> clientTimeStamps.putIfAbsent(client, System.currentTimeMillis()));

//            // Ensure playerTimeStamps map is up-to-date
//            gameState.values().stream().map(Player::getName).forEach(player -> playerTimeStamps.putIfAbsent(player, System.currentTimeMillis()));

            StayAliveData data;
            try {
                socket.receive(packet);
                data = new StayAliveData(packet.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Update timestamps for the client and player
            clientTimeStamps.put(packet.getSocketAddress(), System.currentTimeMillis());
            //playerTimeStamps.put(data.getPlayer(), System.currentTimeMillis());

            disconnectClients();
            //disconnectPlayer();
        }
    }

    /**
     * Disconnects inactive players.
     */
//    private void disconnectPlayer() {
//        Predicate<Map.Entry<String, Long>> predicate = entry -> System.currentTimeMillis() - entry.getValue() > 10000;
//
//        playerTimeStamps.entrySet()
//                .stream()
//                .filter(predicate)
//                .forEach(entry -> gameState.remove(entry.getKey()));
//
//        playerTimeStamps.entrySet().removeIf(predicate);
//    }

    /**
     * Disconnects inactive clients.
     */
    private void disconnectClients() {
        Predicate<Map.Entry<SocketAddress, Long>> predicate = entry -> System.currentTimeMillis() - entry.getValue() > 10000;

        clientTimeStamps.entrySet()
                .stream()
                .filter(predicate)
                .forEach(entry -> clients.disconnect(entry.getKey()));

        clientTimeStamps.entrySet().removeIf(predicate);
    }
}
