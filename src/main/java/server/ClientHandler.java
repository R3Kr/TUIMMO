package server;

import game.Player;
import game.actions.Action;
import protocol.ActionData;
import protocol.ActionDataFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;

/**
 * The ClientHandler class manages incoming client messages and performs actions based on the received data.
 */
public class ClientHandler implements Runnable {

    private GameState gameState;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private List<SocketAddress> clients;

    /**
     * Constructs a ClientHandler object with the specified game state, socket, and list of clients.
     *
     * @param gameState The game state to manage player information.
     * @param socket    The DatagramSocket used to communicate with clients.
     * @param clients   The list of client socket addresses.
     */
    public ClientHandler(GameState gameState, DatagramSocket socket, List<SocketAddress> clients) {
        this.gameState = gameState;
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clients = clients;
    }

    @Override
    public void run() {
        while (true) {
            ActionData data;
            try {
                socket.receive(packet);
                data = ActionDataFactory.createData(packet.getData());
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            if (!clients.contains(packet.getSocketAddress())) {
                clients.add(packet.getSocketAddress());
            }

            Player player = gameState.getMut(data.getPlayerName());

            if (player == null) {
                gameState.put(data.getPlayerName(), new Player(data.getPlayerName(), 10, 10));
            } else {
                ActionDataFactory.createAction(gameState, data).ifPresent(Action::perform);
            }

            System.out.printf("%s %d %d", data.getPlayerName(), gameState.get(data.getPlayerName()).getX(), gameState.get(data.getPlayerName()).getY());
            System.out.println(gameState);
            System.out.println(clients);
        }
    }
}
