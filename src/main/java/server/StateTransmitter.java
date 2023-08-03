package server;

import game.Player;
import protocol.ServerPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StateTransmitter implements Runnable{
    private static final int TICKS = 30;
    private ConcurrentHashMap<String, Player> gameState;
    private DatagramSocket socket;


    private ServerPacket sp;
    private List<SocketAddress> clients;

    public StateTransmitter(ConcurrentHashMap<String, Player> gameState, DatagramSocket socket, List<SocketAddress> clients) {
        this.gameState = gameState;
        this.socket = socket;
        this.sp = new ServerPacket(new DatagramPacket(new byte[1024], 1024));
        this.clients = clients;
    }

    @Override
    public void run() {
        while (true){
            try {
                sp.writeData(gameState.values());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            clients.forEach(c ->{
                sp.getPacket().setSocketAddress(c);
                try {
                    socket.send(sp.getPacket());
                } catch (IOException e) {
                    System.err.println(e);
                }
            });


            try {
                Thread.sleep(1000/TICKS);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

    }
}
