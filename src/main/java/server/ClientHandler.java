package server;

import game.Player;
import protocol.ActionBuilder;
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
                gameState.put(data.getPlayerName(), new Player(data.getPlayerName(), 10, 10));
            }
            else {
                new ActionBuilder(gameState).setPlayer(data.getPlayerName()).setDirection(data.getDirection()).build().perform();
            }


            System.out.printf("%s %d %d", data.getPlayerName(), gameState.get(data.getPlayerName()).getX(), gameState.get(data.getPlayerName()).getY());
            System.out.println(gameState);
            System.out.println(clients);
            //packet.setData("pong".getBytes());
            //socket.send(packet);

        }
    }
}
