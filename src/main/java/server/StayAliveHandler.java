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

public class StayAliveHandler implements Runnable{

    private GameState gameState;
    private DatagramSocket socket;

    private List<SocketAddress> clients;

    private DatagramPacket packet;

    private Map<SocketAddress, Long> clientTimeStamps;
    private Map<String, Long> playerTimeStamps;

    public StayAliveHandler(GameState gameState, List<SocketAddress> clients) throws SocketException {
        this.gameState = gameState;
        this.socket = new DatagramSocket(6970);
        this.clients = clients;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clientTimeStamps = new HashMap<>();
        this.playerTimeStamps = new HashMap<>();


    }

    @Override
    public void run() {
        while (true){


            System.out.println(clientTimeStamps);
            System.out.println(playerTimeStamps);

            if (clients.size() > clientTimeStamps.size()){
                for (SocketAddress client: clients) {
                    clientTimeStamps.putIfAbsent(client, System.currentTimeMillis());
                }
            }

            if (gameState.size() > playerTimeStamps.size()){
                for (String player: gameState.values().stream().map(Player::getName).toList()){
                    playerTimeStamps.putIfAbsent(player, System.currentTimeMillis());
                }
            }

            StayAliveData data;
            try {
                socket.receive(packet);
                data = new StayAliveData(packet.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            clientTimeStamps.put(packet.getSocketAddress(), System.currentTimeMillis());
            playerTimeStamps.put(data.getPlayer(), System.currentTimeMillis());

            disconnectClients();
            disconnectPlayer();


        }



    }

    private void disconnectPlayer() {
        Predicate<Map.Entry<String, Long>> predicate = entry -> System.currentTimeMillis() - entry.getValue() > 10000;

        playerTimeStamps.entrySet()
                .stream()
                .filter(predicate)
                .forEach(entry -> gameState.remove(entry.getKey()));

        playerTimeStamps.entrySet().removeIf(predicate);
    }

    private void disconnectClients() {
        Predicate<Map.Entry<SocketAddress, Long>> predicate = entry -> System.currentTimeMillis() - entry.getValue() > 10000;

        clientTimeStamps.entrySet()
                .stream()
                .filter(predicate)
                .forEach(entry -> clients.remove(entry.getKey()));

        clientTimeStamps.entrySet().removeIf(predicate);
    }
}
