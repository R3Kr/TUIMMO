package game.systems;

import client.animations.Animation;
import client.animations.AttackAnimation;
import client.animations.CoolAnimation;
import game.Attack;
import game.Direction;
import game.Move;
import game.components.Player;
import com.googlecode.lanterna.input.KeyStroke;
import protocol.data.AttackSignal;
import protocol.data.CoolSignal;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InputHandlingSystem implements System{
    private Queue<KeyStroke> keyStrokeQueue;
    private List<Animation> animations;

    private Player posComponent;
    private String player;

    private Consumer<Object> sendToServer;
    Supplier<List<Attack>> createAttacks;

    private Move moveUp;
    private Move moveDown;
    private Move moveLeft;
    private Move moveRight;

    public InputHandlingSystem(Queue<KeyStroke> keyStrokeQueue, List<Animation> animations, Player posComponent, String player, Consumer<Object> sendToServer, Supplier<List<Attack>> createAttacks) {
        this.keyStrokeQueue = keyStrokeQueue;
        this.animations = animations;
        this.posComponent = posComponent;
        this.sendToServer = sendToServer;
        this.player = player;
        this.createAttacks = createAttacks;

        this.moveUp = new Move(posComponent, Direction.UP);
        this.moveDown = new Move(posComponent, Direction.DOWN);
        this.moveLeft = new Move(posComponent, Direction.LEFT);
        this.moveRight = new Move(posComponent, Direction.RIGHT);
    }

    @Override
    public void update() {
        while (!keyStrokeQueue.isEmpty()){
            handle(keyStrokeQueue.poll());
        }

    }

    private void handle(KeyStroke keyStroke){
        switch (keyStroke.getKeyType()) {
            case ArrowUp:
                moveUp.perform();
                sendToServer.accept(Direction.UP);
                break;
            case ArrowDown:
                moveDown.perform();
                sendToServer.accept(Direction.DOWN);
                break;
            case ArrowLeft:
                moveLeft.perform();
                sendToServer.accept(Direction.LEFT);
                break;
            case ArrowRight:
                moveRight.perform();
                sendToServer.accept(Direction.RIGHT);
                break;
            case Backspace:

                animations.add(new AttackAnimation(posComponent));
                createAttacks.get().forEach(Attack::perform);
                sendToServer.accept(new AttackSignal());
                break;
            case Enter:

                animations.add(new CoolAnimation(posComponent));
                sendToServer.accept(new CoolSignal());
                break;

            default:
                break;
        }
    }
}
