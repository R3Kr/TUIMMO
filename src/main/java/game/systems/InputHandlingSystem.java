package game.systems;

import client.animations.*;
import game.actions.Attack;
import game.CooldownState;
import game.actions.ChangeZone;
import game.actions.Direction;
import game.actions.Move;
import game.components.Player;
import com.googlecode.lanterna.input.KeyStroke;
import game.effects.Effect;
import game.effects.RegenEffect;
import protocol.data.*;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InputHandlingSystem implements System{

    private Queue<KeyStroke> keyStrokeQueue;
    private Queue<Effect> effectQueue;
    private List<Animation> animations;

    private Player player;


    private Consumer<Object> sendToServer;
    Supplier<List<Attack>> createAttacks;

    private Move moveUp;
    private Move moveDown;
    private Move moveLeft;
    private Move moveRight;



    private CooldownState cantAttack;
    private CooldownState cantBlock;
    private CooldownState cantRegen;

    public InputHandlingSystem(Queue<KeyStroke> keyStrokeQueue, Queue<Effect> effectQueue, List<Animation> animations, Player posComponent, Consumer<Object> sendToServer, Supplier<List<Attack>> createAttacks, CooldownState canAttack, CooldownState canBlock, CooldownState canRegen) {
        this.keyStrokeQueue = keyStrokeQueue;
        this.effectQueue = effectQueue;
        this.animations = animations;
        this.player = posComponent;
        this.sendToServer = sendToServer;

        this.createAttacks = createAttacks;

        this.moveUp = new Move(posComponent, Direction.UP);
        this.moveDown = new Move(posComponent, Direction.DOWN);
        this.moveLeft = new Move(posComponent, Direction.LEFT);
        this.moveRight = new Move(posComponent, Direction.RIGHT);
        this.cantAttack = canAttack;
        this.cantBlock = canBlock;
        this.cantRegen = canRegen;
    }

    @Override
    public void update() {
        while (!keyStrokeQueue.isEmpty()){
            handle(keyStrokeQueue.poll());
        }

    }

    private void handle(KeyStroke keyStroke){



        switch (keyStroke.getKeyType()) {


            case Backspace -> {
                if (keyStroke.isShiftDown()){
                    if (cantBlock.test(keyStroke.getEventTime())){
                        break;
                    }
                    cantBlock.set(keyStroke.getEventTime());
                    animations.add(new BlockAnimation(player));
                    sendToServer.accept(new BlockSignal());
                    break;
                }


                if (cantAttack.test(keyStroke.getEventTime())) {
                    break;
                }
                cantAttack.set(keyStroke.getEventTime());
                animations.add(new AttackAnimation(player));
                createAttacks.get().forEach(Attack::perform);
                sendToServer.accept(new AttackSignal());
            }
            case Enter -> {
                if (keyStroke.isShiftDown()){
                    if (cantRegen.test(keyStroke.getEventTime())){
                        break;
                    }
                    cantRegen.set(keyStroke.getEventTime());
                    effectQueue.offer(new RegenEffect(player));
                    animations.add(new RegenAnimation(player));
                    sendToServer.accept(new RegenSignal());
                    break;
                }

                animations.add(new CoolAnimation(player));
                sendToServer.accept(new CoolSignal());
            }
            case ArrowRight -> {
                if (player.getZoneID() == 0){
                    sendToServer.accept(new ChangeZoneSignal(1, Direction.UP));
                }
                else {
                    sendToServer.accept(new ChangeZoneSignal(0, Direction.UP));
                }
            }
            case Character -> {
                switch (keyStroke.getCharacter()){
                    case 'w' -> {
                        moveUp.perform();
                        sendToServer.accept(Direction.UP);
                    }
                    case 's' -> {
                        moveDown.perform();
                        sendToServer.accept(Direction.DOWN);
                    }
                    case 'a' -> {
                        moveLeft.perform();
                        sendToServer.accept(Direction.LEFT);
                    }
                    case 'd' -> {
                        moveRight.perform();
                        sendToServer.accept(Direction.RIGHT);
                    }

                    default -> {

                    }
                }
            }
            default -> {
            }
        }


    }
}
