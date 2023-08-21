package game.systems;

import client.animations.Animation;
import client.animations.AttackAnimation;
import client.animations.BlockAnimation;
import client.animations.CoolAnimation;
import game.Attack;
import game.CooldownState;
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

    private Queue<KeyStroke> keyStrokeQueue;
    private List<Animation> animations;

    private Player posComponent;


    private Consumer<Object> sendToServer;
    Supplier<List<Attack>> createAttacks;

    private Move moveUp;
    private Move moveDown;
    private Move moveLeft;
    private Move moveRight;



    private CooldownState canAttack;
    private CooldownState canBlock;

    public InputHandlingSystem(Queue<KeyStroke> keyStrokeQueue, List<Animation> animations, Player posComponent, Consumer<Object> sendToServer, Supplier<List<Attack>> createAttacks, CooldownState canAttack, CooldownState canBlock) {
        this.keyStrokeQueue = keyStrokeQueue;
        this.animations = animations;
        this.posComponent = posComponent;
        this.sendToServer = sendToServer;

        this.createAttacks = createAttacks;

        this.moveUp = new Move(posComponent, Direction.UP);
        this.moveDown = new Move(posComponent, Direction.DOWN);
        this.moveLeft = new Move(posComponent, Direction.LEFT);
        this.moveRight = new Move(posComponent, Direction.RIGHT);
        this.canAttack = canAttack;
        this.canBlock = canBlock;
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
                    if (canBlock.test(keyStroke.getEventTime())){
                        break;
                    }
                    canBlock.set(keyStroke.getEventTime());
                    animations.add(new BlockAnimation(posComponent));
                    sendToServer.accept(new BlockSignal());
                    break;
                }


                if (canAttack.test(keyStroke.getEventTime())) {
                    break;
                }
                canAttack.set(keyStroke.getEventTime());
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
