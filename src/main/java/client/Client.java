package client;

import client.states.LoginState;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import game.actions.Action;
import game.actions.Move;
import game.Direction;
import game.Player;
import protocol.AttackData;
import protocol.MoveData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Client class represents a game client using Lanterna library for terminal-based UI.
 */
public class Client {
    private Terminal terminal;
    private Screen screen;
    private TextGraphics playerGraphics;
    private TextGraphics terrainGraphics;
    private TextGraphics uiGraphics;
    private Player player;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private ExecutorService threadPool;
    private ConcurrentHashMap<String, Player> players;
    private boolean isRunning = true;
    private InetAddress address;
    private HpBar hpBar;

    public ClientContext context;

    /**
     * Constructs a new Client instance.
     *
     * @param playerName The name of the player.
     * @param address    The IP address of the server.
     * @throws IOException       If an I/O error occurs.
     * @throws NotBoundException If a binding-related error occurs.
     */
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
        this.packet = new DatagramPacket(new byte[1024], 1024, InetAddress.getByName(address), 6969);
        this.players = new ConcurrentHashMap<>();
        players.put(playerName, player);
        this.terrainGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.BLACK).setBackgroundColor(TextColor.ANSI.WHITE);
        this.uiGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.hpBar = new HpBar(player);
        this.context = new ClientContext(new LoginState(screen));
    }

    /**
     * Runs the game client.
     *
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    private void run() throws IOException, InterruptedException {
        threadPool.execute(new ServerListener(players, socket));
        threadPool.execute(new StayAliveSender(player, socket, address));
        screen.startScreen();
        screen.clear();
        playerGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        playerGraphics.setBackgroundColor(TextColor.ANSI.BLUE);

        Action moveUp = new Move(player, Direction.UP);
        Action moveDown = new Move(player, Direction.DOWN);
        Action moveLeft = new Move(player, Direction.LEFT);
        Action moveRight = new Move(player, Direction.RIGHT);

        while (isRunning) {
            screen.refresh();
            screen.clear();

            KeyStroke keyStroke = screen.pollInput();

            // Handle input
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case ArrowUp:
                        moveUp.perform();
                        packet.setData(new MoveData(player.getName(), Direction.UP).read());
                        break;
                    case ArrowDown:
                        moveDown.perform();
                        packet.setData(new MoveData(player.getName(), Direction.DOWN).read());
                        break;
                    case ArrowLeft:
                        moveLeft.perform();
                        packet.setData(new MoveData(player.getName(), Direction.LEFT).read());
                        break;
                    case ArrowRight:
                        moveRight.perform();
                        packet.setData(new MoveData(player.getName(), Direction.RIGHT).read());
                        break;
                    case Backspace:
                        Optional<Player> player2 = players.values().stream().filter(p -> p != player && player.isCloseTo(p)).findFirst();
                        if (player2.isPresent()){
                            packet.setData(new AttackData(player.getName(), player2.get().getName()).read());
                        }
                        break;
                    case Escape:
                        isRunning = false;
                        break;
                    default:
                        break;
                }
                socket.send(packet);
            }

            renderPlayers();
            renderTerrain();
            renderUI();

            Thread.sleep(20);
        }

        threadPool.shutdownNow();
        screen.stopScreen();

    }

    private void renderUI() {
        uiGraphics.putString(HpBar.POSITION, hpBar.getString());
    }

    private void renderPlayers() {
        players.values().forEach(p -> {
            playerGraphics.putString(p.getX(), p.getY(), p.getName());
        });
    }

    private void renderTerrain() {
        terrainGraphics.drawLine(0, 0, 0, 23, 'E');
        terrainGraphics.drawLine(0, 23, 40, 23, '@');
    }

    /**
     * The main method to start the game client.
     *
     * @param args Command-line arguments containing player name and server IP address.
     * @throws IOException          If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     * @throws NotBoundException    If a binding-related error occurs.
     */
    public static void main(String[] args) throws IOException, InterruptedException, NotBoundException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Needs 2 arguments, name and ipaddress");
        }
        Client client = new Client(args[0].substring(0, 2), args[1]);
        //client.run();   //temporärt
        client.context.run();
    }
}
