package client;

import game.Player;
import protocol.StayAliveData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * The StayAliveSender class periodically sends stay-alive data to the server to maintain the connection.
 */
public class StayAliveSender implements Runnable {

    public static final int SEND_RATE = 1000;

    private DatagramSocket socket;
    private DatagramPacket packet;
    private StayAliveData data;

    /**
     * Constructs a new StayAliveSender instance.
     *
     * @param player  The player associated with the stay-alive data.
     * @param socket  The DatagramSocket for communication.
     * @param address The InetAddress of the server.
     * @throws IOException If an I/O error occurs.
     */
    public StayAliveSender(Player player, DatagramSocket socket, InetAddress address) throws IOException {
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024, address, 6970);
        packet.setData(new StayAliveData(player.getName()).read());
    }

    /**
     * Sends stay-alive data to the server at regular intervals.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("going to send");
                socket.send(packet);
                System.out.println("just sent");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try {
                Thread.sleep(SEND_RATE);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("StayAliveSender shutting down");
    }
}
