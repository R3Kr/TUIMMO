package server;

import game.Player;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Server class represents the game server that handles incoming connections and manages game state.
 */
public class Server {

    private DatagramSocket socket;

    private GameState gameState;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private List<SocketAddress> clients;

    /**
     * Constructs a Server object, initializes the socket, game state, and clients list.
     *
     * @throws SocketException    If there is an error creating the DatagramSocket.
     * @throws UnknownHostException If the host address is unknown.
     */
    public Server() throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(6969, InetAddress.getByName("0.0.0.0"));
        this.gameState = new GameState();
        this.clients = new ArrayList<>();
    }

    /**
     * The main method that starts the server.
     *
     * @param args Command line arguments (not used).
     * @throws IOException If there is an I/O error.
     */
    public static void main(String[] args) throws IOException {
        new Server().run();
    }

    /**
     * Starts the server by creating and executing necessary threads.
     *
     * @throws IOException If there is an I/O error.
     */
    private void run() throws IOException {
        ClientHandler clientHandler = new ClientHandler(gameState, socket, clients);
        executorService.execute(clientHandler);
        executorService.execute(new StateTransmitter(gameState, socket, clients));
        executorService.execute(new StayAliveHandler(gameState, clients));
    }
}
