package server;

import game.Player;
import game.actions.Action;
import protocol.ActionData;
import protocol.ActionDataFactory;
import protocol.AnimationData;
import protocol.AnimationDataFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Queue;

/**
 * The ClientHandler class manages incoming client messages and performs actions based on the received data.
 */
public class ClientHandler implements Runnable {

    private GameState gameState;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private Clients clients;

    private Queue<AnimationData> animationDataQueue;

    /**
     * Constructs a ClientHandler object with the specified game state, socket, and list of clients.
     *
     * @param gameState The game state to manage player information.
     * @param socket    The DatagramSocket used to communicate with clients.
     * @param clients   The list of client socket addresses.
     */
    public ClientHandler(GameState gameState, DatagramSocket socket, Clients clients, Queue<AnimationData> animationDataQueue) {
        this.gameState = gameState;
        this.socket = socket;
        this.packet = new DatagramPacket(new byte[1024], 1024);
        this.clients = clients;
        this.animationDataQueue = animationDataQueue;
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
                clients.connect(packet.getSocketAddress(), data.getPlayerName());
            }

            Player player = gameState.getPlayerMut(data.getPlayerName());

            if (player == null) {
                gameState.put(data.getPlayerName(), new Player(data.getPlayerName(), 10, 10));
            } else {
                AnimationDataFactory.createAnimation(data).ifPresent(a -> animationDataQueue.offer(a));
                ActionDataFactory.createAction(gameState, data).ifPresent(Action::perform);
            }

            System.out.printf("%s %d %d", data.getPlayerName(), gameState.get(data.getPlayerName()).getX(), gameState.get(data.getPlayerName()).getY());
            System.out.println(gameState);
            System.out.println(clients);
        }
    }
}
