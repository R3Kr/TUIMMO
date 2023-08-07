package client;

import com.googlecode.lanterna.TerminalSize;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private Terminal terminal;
    private Screen screen;
    private TextGraphics playerGraphics;
    private TextGraphics terrainGraphics;

    private TextGraphics uiGraphics;

    private Player player;

    private DatagramSocket socket;

    private ClientPacket cp;
    private ExecutorService threadPool;

    private ConcurrentHashMap<String, Player> players;

    private boolean isRunning = true;

    private InetAddress address;

    private HpBar hpBar;

    public Client(String playerName, String address) throws IOException, NotBoundException {
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        terminalFactory.setInitialTerminalSize(new TerminalSize(80, 27));
        terminal = terminalFactory.setPreferTerminalEmulator(true).createTerminal();
        screen = new TerminalScreen(terminal);
        playerGraphics = screen.newTextGraphics();
        this.player = new Player(playerName, 5, 5);
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
        this.threadPool = Executors.newCachedThreadPool();
        this.cp = new ClientPacket(new DatagramPacket(new byte[1024], 1024, InetAddress.getByName(address), 6969));
        this.players = new ConcurrentHashMap<>();
        this.terrainGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.BLACK).setBackgroundColor(TextColor.ANSI.WHITE);
        this.uiGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.hpBar = new HpBar(player);

    }

    private void run() throws IOException, InterruptedException {
        threadPool.execute(new ServerListener(players, socket));
        threadPool.execute(new StayAliveSender(player, socket, address));
        screen.startScreen(); // screens must be started
        screen.clear();

        playerGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        playerGraphics.setBackgroundColor(TextColor.ANSI.BLUE);


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
            renderTerrain();
            renderUI();



            Thread.sleep(20);
        }

        screen.stopScreen(); // screens must be stopped when done
        threadPool.shutdown();

    }

    private void renderUI() {
        uiGraphics.putString(HpBar.POSITION, hpBar.getString());
    }

    private void renderPlayers(){
        players.values().forEach(p -> {
            playerGraphics.putString(p.getX(), p.getY(), p.getName());
        });
    }

    private void renderTerrain(){
        terrainGraphics.drawLine(0, 0, 0, 23, 'E');
        terrainGraphics.drawLine(0, 23, 40, 23, '@');

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