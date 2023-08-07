package server;


import game.Player;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private DatagramSocket socket;



    private GameState gameState;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    private List<SocketAddress> clients;



    public Server() throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(6969, InetAddress.getByName("0.0.0.0"));
        this.gameState = new GameState();
        this.clients = new ArrayList<>();
    }

    public static void main(String[] args) throws IOException {
        new Server().run();
    }

    private void run() throws IOException {
        ClientHandler clientHandler = new ClientHandler(gameState, socket, clients);
        executorService.execute(clientHandler);
        executorService.execute(new StateTransmitter(gameState, socket, clients));
        executorService.execute(new StayAliveHandler(gameState, clients));
    }
}
