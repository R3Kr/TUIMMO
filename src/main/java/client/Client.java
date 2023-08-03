package client;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import protocol.ClientPacket;
import game.Direction;
import game.Player;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private Terminal terminal;
    private Screen screen;
    private TextGraphics tg;

    private Player player;

    private DatagramSocket socket;

    private ClientPacket cp;
    private ExecutorService threadPool;

    private ConcurrentHashMap<String, Player> players;

    private boolean isRunning = true;

    public Client(String playerName, String address) throws IOException, NotBoundException {
        terminal = new DefaultTerminalFactory().setPreferTerminalEmulator(true).createTerminal();
        screen = new TerminalScreen(terminal);
        tg = screen.newTextGraphics();
        this.player = new Player(playerName, 5, 5);
        this.socket = new DatagramSocket();
        this.threadPool = Executors.newCachedThreadPool();
        this.cp = new ClientPacket(new DatagramPacket(new byte[1024], 1024, InetAddress.getByName(address), 6969));
        this.players = new ConcurrentHashMap<>();

    }

    private void run() throws IOException, InterruptedException {
        threadPool.execute(new ServerListener(players, socket));

        screen.startScreen(); // screens must be started
        screen.clear();



        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.BLUE);


        while (isRunning) {


            screen.refresh();
            screen.clear();
            // Read input (non-blocking)
            KeyStroke keyStroke = screen.pollInput();


            // Handle input
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case ArrowUp -> player.move(Direction.UP);
                    case ArrowDown -> player.move(Direction.DOWN);
                    case ArrowLeft -> player.move(Direction.LEFT);
                    case ArrowRight -> player.move(Direction.RIGHT);
                    case Escape -> {
                        isRunning = false;
                    }
                    default -> {
                    }
                }


                cp.writeData(player.getName(), player.getX(), player.getY());
                socket.send(cp.getPacket());
            }

            //System.out.println(players);
            //tg.putString(player.getX(), player.getY(), player.getName());
            renderPlayers();

            tg.drawLine(0, 0, 30, 40, 'E');


            Thread.sleep(20);
        }

        screen.stopScreen(); // screens must be stopped when done
        threadPool.shutdown();

    }

    private void renderPlayers(){
        players.values().forEach(p -> {
            tg.putString(p.getX(), p.getY(), p.getName());
        });
    }

    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Needs 2 arguments, name and ipaddress");
        }
        Client client = new Client(args[0].substring(0, 2), args[1]);
        client.run();

//        GameStateService gameStateService1 = (GameStateService) Naming.lookup("rmi://localhost:6969/gamestate");
//        gameStateService1.addPlayer(new Player());
//
//        System.out.println(gameStateService1.getPlayers());


    }


}