package server;

import game.Player;
import protocol.AnimationData;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * The Server class represents the game server that handles incoming connections and manages game state.
 */
public class Server {

    private DatagramSocket socket;

    private GameState gameState;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private Clients clients;

    private BlockingQueue<AnimationData> animationDataBlockingQueue;

    /**
     * Constructs a Server object, initializes the socket, game state, and clients list.
     *
     * @throws SocketException    If there is an error creating the DatagramSocket.
     * @throws UnknownHostException If the host address is unknown.
     */
    public Server() throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(6969, InetAddress.getByName("0.0.0.0"));
        this.gameState = new GameState();
        this.clients = new Clients(gameState);
        this.animationDataBlockingQueue = new LinkedBlockingQueue<>();
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
        ClientHandler clientHandler = new ClientHandler(gameState, socket, clients, animationDataBlockingQueue);
        executorService.execute(clientHandler);
        executorService.execute(new StateTransmitter(gameState, socket, clients));
        executorService.execute(new StayAliveHandler(gameState, clients));
        executorService.execute(new AnimationDataTransmitter(animationDataBlockingQueue, clients, socket));
    }
}
