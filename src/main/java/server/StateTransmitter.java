package server;

import protocol.ServerData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;

public class StateTransmitter implements Runnable{
    private static final int TICKS = 30;
    private GameState gameState;
    private DatagramSocket socket;


    private DatagramPacket packet;
    private List<SocketAddress> clients;

    public StateTransmitter(GameState gameState, DatagramSocket socket, List<SocketAddress> clients) {
        this.gameState = gameState;
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clients = clients;
    }

    @Override
    public void run() {
        while (true){


            if (!gameState.sentStateUpdate.get()){
                sendData();
                gameState.sentStateUpdate.compareAndSet(false, true);
            }

            try {
                Thread.sleep(1000/TICKS);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }

    }

    private void sendData(){
        try {
            packet.setData(new ServerData(gameState.values()).read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clients.forEach(c ->{
            packet.setSocketAddress(c);
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.err.println(e);
            }
        });
    }
}
