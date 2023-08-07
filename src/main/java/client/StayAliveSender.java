package client;

import game.Player;
import protocol.StayAliveData;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class StayAliveSender implements Runnable{

    public static final int SEND_RATE = 1000;

    private DatagramSocket socket;
    private DatagramPacket packet;

    private StayAliveData data;

    public StayAliveSender(Player player, DatagramSocket socket, InetAddress address) throws IOException {

        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024, address, 6970);
        packet.setData(new StayAliveData(player.getName()).read());
    }

    @Override
    public void run() {
        while (true){
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(SEND_RATE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
