package client;

import game.Player;
import protocol.StayAlivePacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class StayAliveSender implements Runnable{

    private DatagramSocket socket;
    private StayAlivePacket sap;

    public StayAliveSender(Player player, DatagramSocket socket, InetAddress address) throws IOException {

        this.socket = socket;
        this.sap = new StayAlivePacket(new DatagramPacket(new byte[1024], 1024, address, 6970));
        sap.writeData(player.getName());
    }

    @Override
    public void run() {
        while (true){
            try {
                socket.send(sap.getPacket());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(StayAlivePacket.SEND_RATE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


    }
}
