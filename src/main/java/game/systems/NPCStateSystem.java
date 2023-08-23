package game.systems;

import game.actions.Action;
import game.actions.Attack;
import game.actions.Direction;
import game.actions.Move;
import game.components.GameObject;
import game.components.NPC;
import game.components.Player;
import protocol.data.AnimationData;
import protocol.data.DisconnectGameobject;
import protocol.data.StateUpdateData;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NPCStateSystem implements System{
    private Consumer<StateUpdateData> broadcastNPCUpdate;
    private Supplier<Stream<NPC>> npcSupplier;
    private Supplier<Optional<Player>> playerSupplier;


    private Queue<DisconnectGameobject> disconnectGameobjectQueue;

    private Queue<Action> attackQueue;

    private Queue<AnimationData> animationDataQueue;
    private Queue<GameObject> objectsToUpdate;
    private Function<GameObject, List<Attack>> createAttacks;

    private Random r = new Random();

    public NPCStateSystem(Consumer<StateUpdateData> broadcastStateUpdate, Supplier<Stream<NPC>> npcSupplier, Supplier<Optional<Player>> playerSupplier, Queue<DisconnectGameobject> disconnectGameobjectQueue, Queue<Action> attackQueue, Queue<AnimationData> animationDataQueue, Queue<GameObject> objectsToUpdate, Function<GameObject, List<Attack>> createAttacks) {
        this.broadcastNPCUpdate = broadcastStateUpdate;
        this.npcSupplier = npcSupplier;
        this.playerSupplier = playerSupplier;

        this.disconnectGameobjectQueue = disconnectGameobjectQueue;
        this.attackQueue = attackQueue;
        this.animationDataQueue = animationDataQueue;
        this.objectsToUpdate = objectsToUpdate;
        this.createAttacks = createAttacks;
    }

    @Override
    public void update() {

        Optional<Player> player = playerSupplier.get();

        npcSupplier.get()
                .filter(npc -> 0 == r.nextInt(100) && npc.getState().equals("neutral"))
                .forEach(npc -> {
                    Direction direction = Direction.fromShort((short) r.nextInt(4));
                    Action move = new Move(npc, direction).perform();
                    move.getInvolved().forEach(objectsToUpdate::add);

                });

        npcSupplier.get().filter(npc -> npc.getState().equals("attack") && r.nextInt(10) == 0).forEach(npc -> {
            player.ifPresent(p -> moveToTarget(npc, p));
            animationDataQueue.offer(new AnimationData(69420, npc.getName(), AnimationData.AnimationType.ATTACK));
            createAttacks.apply(npc).forEach(attack -> {
                attackQueue.offer(attack);
                attack.getInvolved().forEach(objectsToUpdate::add);
            });
        });


        npcSupplier.get().filter(npc -> npc.getCurrHp() <= 0).forEach(npc -> disconnectGameobjectQueue.offer(new DisconnectGameobject(npc.getName())));


    }

    private void moveToTarget(GameObject npc, GameObject target){
        int distX = npc.getX() - target.getX();
        int distY = npc.getY() - target.getY();
        Action move;

        if (r.nextBoolean()){
            if (distX <= 0){
                move = new Move(npc, Direction.RIGHT).perform();
            }
            else {
                move = new Move(npc, Direction.LEFT).perform();
            }
        }
        else {
            if (distY <= 0){

                move = new Move(npc, Direction.DOWN).perform();
            }
            else {
                move = new Move(npc, Direction.UP).perform();
            }
        }

        objectsToUpdate.offer(move.getPerformer());

    }
}
