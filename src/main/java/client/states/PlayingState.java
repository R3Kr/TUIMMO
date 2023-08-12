package client.states;

import client.ClientContext;
import client.HpBar;
import client.ServerListener;
import client.StayAliveSender;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import game.Direction;
import game.Player;
import game.actions.Action;
import game.actions.Move;
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

public class PlayingState implements ClientState {
    private Screen screen;
    private TextGraphics playerGraphics;
    private TextGraphics terrainGraphics;
    private TextGraphics uiGraphics;
    private Player player;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private ExecutorService threadPool;
    private ConcurrentHashMap<String, Player> players;
    private InetAddress address;
    private HpBar hpBar;
    private int frames = 0;


    private final Action moveUp;

    private final Action moveDown;

    private final Action moveLeft;

    private final Action moveRight;


    public PlayingState(Screen screen, String playerName, String address) throws IOException {

        this.screen = screen;
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

        threadPool.execute(new ServerListener(players, socket));
        threadPool.execute(new StayAliveSender(player, socket, this.address));
        screen.startScreen();
        screen.clear();
        playerGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        playerGraphics.setBackgroundColor(TextColor.ANSI.BLUE);

        moveUp = new Move(player, Direction.UP);
        moveDown = new Move(player, Direction.DOWN);
        moveLeft = new Move(player, Direction.LEFT);
        moveRight = new Move(player, Direction.RIGHT);

    }

    @Override
    public StateResult tick() throws IOException {


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
                    if (player2.isPresent()) {
                        packet.setData(new AttackData(player.getName(), player2.get().getName()).read());
                    }
                    break;
                case Escape:
                    return StateResult.EXIT;
                default:
                    break;
            }
            socket.send(packet);
        }

        renderAnimation();
        renderPlayers();
        renderTerrain();
        renderUI();
        frames++;


        //threadPool.shutdownNow();
        //screen.stopScreen();
        return StateResult.OK;
    }

    @Override
    public void shutDown() {
        threadPool.shutdownNow();
    }



    private void renderAnimation(){

        switch (frames%11){
            case 1 -> uiGraphics.setCharacter(player.getX()-1, player.getY(), '|');
            case 2 -> uiGraphics.setCharacter(player.getX()-1, player.getY() -1, '°');
            case 3 -> uiGraphics.setCharacter(player.getX(), player.getY() -1, '-');
            case 4 -> uiGraphics.setCharacter(player.getX()+1, player.getY() -1, '-');
            case 5 -> uiGraphics.setCharacter(player.getX()+2, player.getY() -1, '°');
            case 6 -> uiGraphics.setCharacter(player.getX()+2, player.getY(), '|');
            case 7 -> uiGraphics.setCharacter(player.getX()+2, player.getY() +1, '°');
            case 8 -> uiGraphics.setCharacter(player.getX(), player.getY() +1, '-');
            case 9 -> uiGraphics.setCharacter(player.getX() + 1, player.getY() +1, '-');
            case 10 -> uiGraphics.setCharacter(player.getX()-1, player.getY() +1, '°');
        }

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

    private void renderUI() {
        uiGraphics.putString(HpBar.POSITION, hpBar.getString());
    }

}
