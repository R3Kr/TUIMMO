package server;

import protocol.StateData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;

/**
 * The StateTransmitter class is responsible for sending game state updates to clients at regular intervals.
 */
public class StateTransmitter implements Runnable {
    private static final int TICKS = 30;
    private GameState gameState;
    private DatagramSocket socket;

    private DatagramPacket packet;
    private Clients clients;

    private int elapsed_ticks = 0;

    /**
     * Constructs a StateTransmitter object with the specified game state, socket, and clients list.
     *
     * @param gameState The game state containing player information.
     * @param socket    The DatagramSocket used for sending packets.
     * @param clients   The list of client SocketAddresses.
     */
    public StateTransmitter(GameState gameState, DatagramSocket socket, Clients clients) {
        this.gameState = gameState;
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clients = clients;
    }

    /**
     * Continuously runs the state transmission process.
     */
    @Override
    public void run() {
        while (true) {
            if (!gameState.sentStateUpdate.get()) {
                sendData();
                gameState.sentStateUpdate.compareAndSet(false, true);
            }



            try {
                Thread.sleep(1000 / TICKS);
                elapsed_ticks++;
            } catch (InterruptedException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * Sends game state updates to all connected clients.
     */
    private void sendData() {
        try {
            packet.setData(new StateData(gameState.values()).read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clients.forEach(c -> {
            packet.setSocketAddress(c);
            try {
                socket.send(packet);
            } catch (IOException e) {
                System.err.println(e);
            }
        });
    }
}
