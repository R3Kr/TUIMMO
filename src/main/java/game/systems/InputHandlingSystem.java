package game.systems;

import client.animations.Animation;
import client.animations.AttackAnimation;
import client.animations.BlockAnimation;
import client.animations.CoolAnimation;
import game.Attack;
import game.Direction;
import game.Move;
import game.components.Player;
import com.googlecode.lanterna.input.KeyStroke;
import protocol.data.AttackSignal;
import protocol.data.BlockSignal;
import protocol.data.CoolSignal;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InputHandlingSystem implements System{
    private static final int ATTACK_COOLDOWN = 500;
    private static final int BLOCK_COOLDOWN = 5000;
    private Queue<KeyStroke> keyStrokeQueue;
    private List<Animation> animations;

    private Player posComponent;


    private Consumer<Object> sendToServer;
    Supplier<List<Attack>> createAttacks;

    private Move moveUp;
    private Move moveDown;
    private Move moveLeft;
    private Move moveRight;

    private long lastAttack = 0;
    private long lastBlock = 0;

    public InputHandlingSystem(Queue<KeyStroke> keyStrokeQueue, List<Animation> animations, Player posComponent, Consumer<Object> sendToServer, Supplier<List<Attack>> createAttacks) {
        this.keyStrokeQueue = keyStrokeQueue;
        this.animations = animations;
        this.posComponent = posComponent;
        this.sendToServer = sendToServer;

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
            case ArrowUp -> {
                moveUp.perform();
                sendToServer.accept(Direction.UP);
            }
            case ArrowDown -> {
                moveDown.perform();
                sendToServer.accept(Direction.DOWN);
            }
            case ArrowLeft -> {
                moveLeft.perform();
                sendToServer.accept(Direction.LEFT);
            }
            case ArrowRight -> {
                moveRight.perform();
                sendToServer.accept(Direction.RIGHT);
            }
            case Backspace -> {
                if (keyStroke.isShiftDown()){
                    if (keyStroke.getEventTime() - lastBlock < BLOCK_COOLDOWN){
                        break;
                    }
                    lastBlock = keyStroke.getEventTime();
                    animations.add(new BlockAnimation(posComponent));
                    sendToServer.accept(new BlockSignal());
                    break;
                }


                if (keyStroke.getEventTime() - lastAttack < ATTACK_COOLDOWN) {
                    break;
                }
                lastAttack = keyStroke.getEventTime();
                animations.add(new AttackAnimation(posComponent));
                createAttacks.get().forEach(Attack::perform);
                sendToServer.accept(new AttackSignal());
            }
            case Enter -> {
                animations.add(new CoolAnimation(posComponent));
                sendToServer.accept(new CoolSignal());
            }
            default -> {
            }
        }
    }
}
