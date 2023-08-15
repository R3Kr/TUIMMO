package client.states;

import client.HpBar;
import client.animations.Animation;
import client.animations.AnimationList;
import client.animations.AttackAnimation;
import client.animations.CoolAnimation;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import game.actions.Direction;
import game.actions.Action;
import game.actions.Move;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlayingState implements ClientState {
    private Screen screen;
    private TextGraphics playerGraphics;
    private TextGraphics terrainGraphics;
    private TextGraphics uiGraphics;

    private DatagramSocket socket;
    private DatagramPacket packet;
    private ExecutorService threadPool;

    private InetAddress address;
    private HpBar hpBar;

    private List<Animation> animations;




    public PlayingState(Screen screen, String playerName, String address) throws IOException {

        this.screen = screen;
        playerGraphics = screen.newTextGraphics();

        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName(address);
        this.threadPool = Executors.newCachedThreadPool();
        this.packet = new DatagramPacket(new byte[1024], 1024, InetAddress.getByName(address), 6969);
        this.terrainGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.BLACK).setBackgroundColor(TextColor.ANSI.WHITE);
        this.uiGraphics = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.hpBar = new HpBar();
        this.animations = new AnimationList();


        screen.startScreen();
        screen.clear();
        playerGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        playerGraphics.setBackgroundColor(TextColor.ANSI.BLUE);



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
                    //moveUp.perform();

                    break;
                case ArrowDown:
                    //moveDown.perform();

                    break;
                case ArrowLeft:
                    //moveLeft.perform();

                    break;
                case ArrowRight:
                    //moveRight.perform();

                    break;
                case Backspace:

                    //animations.add(new AttackAnimation(player));
                    break;
                case Enter:

                    //animations.add(new CoolAnimation(player));
                    break;
                case Escape:
                    return StateResult.EXIT;
                default:
                    break;
            }

        }


        renderAnimations();

        renderTerrain();
        renderUI();



        //threadPool.shutdownNow();
        //screen.stopScreen();
        return StateResult.OK;
    }

    @Override
    public void shutDown() {
        threadPool.shutdownNow();
    }



    private void renderAnimations(){
        for (Animation a : animations){
            a.renderWith(uiGraphics);
        }


//        switch (frames%11){
//            case 1 -> uiGraphics.setCharacter(player.getX()-1, player.getY(), '|');
//            case 2 -> uiGraphics.setCharacter(player.getX()-1, player.getY() -1, '°');
//            case 3 -> uiGraphics.setCharacter(player.getX(), player.getY() -1, '-');
//            case 4 -> uiGraphics.setCharacter(player.getX()+1, player.getY() -1, '-');
//            case 5 -> uiGraphics.setCharacter(player.getX()+2, player.getY() -1, '°');
//            case 6 -> uiGraphics.setCharacter(player.getX()+2, player.getY(), '|');
//            case 7 -> uiGraphics.setCharacter(player.getX()+2, player.getY() +1, '°');
//            case 8 -> uiGraphics.setCharacter(player.getX(), player.getY() +1, '-');
//            case 9 -> uiGraphics.setCharacter(player.getX() + 1, player.getY() +1, '-');
//            case 10 -> {
//                uiGraphics.setCharacter(player.getX()-1, player.getY() +1, '°');
//                attacking = false;
//            }
//        }

    }


    private void renderTerrain() {
        terrainGraphics.drawLine(0, 0, 0, 23, 'E');
        terrainGraphics.drawLine(0, 23, 40, 23, '@');
    }

    private void renderUI() {
        uiGraphics.putString(HpBar.POSITION, hpBar.getString());
    }

}
