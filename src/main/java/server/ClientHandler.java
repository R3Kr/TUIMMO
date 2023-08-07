package server;

import game.Player;
import protocol.ClientData;
import protocol.Data;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;

public class ClientHandler implements Runnable{

    private GameState gameState;
    private DatagramSocket socket;


    private DatagramPacket packet;

    private List<SocketAddress> clients;
    public ClientHandler(GameState gameState, DatagramSocket socket, List<SocketAddress> clients) {
        this.gameState = gameState;
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clients = clients;

    }

    @Override
    public void run() {

        while (true){
            ClientData data;
            try {
                socket.receive(packet);
                data = new ClientData(packet.getData());
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            if (!clients.contains(packet.getSocketAddress())){
                clients.add(packet.getSocketAddress());
            }

            Player player = gameState.getMut(data.getPlayerName());

            if (player == null){
                gameState.put(data.getPlayerName(), new Player(data.getPlayerName(), data.getX(), data.getY()));
            }
            else {
                player.setX(data.getX());
                player.setY(data.getY());
            }


            System.out.printf("%s %d %d", data.getPlayerName(), data.getX(), data.getY());
            System.out.println(gameState);
            System.out.println(clients);
            //packet.setData("pong".getBytes());
            //socket.send(packet);

        }
    }
}
