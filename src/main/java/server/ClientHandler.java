package server;

import game.Player;
import protocol.ClientPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler implements Runnable{

    private ConcurrentHashMap<String, Player> gameState;
    private DatagramSocket socket;


    private ClientPacket cp;

    private List<SocketAddress> clients;
    public ClientHandler(ConcurrentHashMap<String, Player> gameState, DatagramSocket socket, List<SocketAddress> clients) {
        this.gameState = gameState;
        this.socket = socket;
        this.cp = new ClientPacket(new DatagramPacket(new byte[1024], 1024));
        this.clients = clients;

    }

    @Override
    public void run() {

        while (true){
            try {
                socket.receive(cp.getPacket());
                cp.readData();
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            if (!clients.contains(cp.getPacket().getSocketAddress())){
                clients.add(cp.getPacket().getSocketAddress());
            }

            Player player = gameState.get(cp.getPlayerName());

            if (player == null){
                gameState.put(cp.getPlayerName(), new Player(cp.getPlayerName(), cp.getX(), cp.getY()));
            }
            else {
                player.setX(cp.getX());
                player.setY(cp.getY());
            }


            System.out.printf("%s %d %d%n", cp.getPlayerName(), cp.getX(), cp.getY());
            System.out.println(gameState);
            System.out.println(clients);
            //packet.setData("pong".getBytes());
            //socket.send(packet);

        }
    }
}
