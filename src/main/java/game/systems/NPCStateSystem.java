package game.systems;

import game.Action;
import game.Attack;
import game.Direction;
import game.Move;
import game.components.GameObject;
import game.components.NPC;
import protocol.data.AnimationData;
import protocol.data.DisconnectGameobject;
import protocol.data.StateData;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NPCStateSystem implements System{
    private Consumer<StateData> broadcastNPCUpdate;
    private Supplier<Stream<NPC>> npcSupplier;


    private Queue<DisconnectGameobject> disconnectGameobjectQueue;

    private Queue<Action> attackQueue;

    private Queue<AnimationData> animationDataQueue;
    private Function<GameObject, List<Attack>> createAttacks;

    private Random r = new Random();

    public NPCStateSystem(Consumer<StateData> broadcastStateUpdate, Supplier<Stream<NPC>> npcSupplier, Queue<DisconnectGameobject> disconnectGameobjectQueue, Queue<Action> attackQueue, Queue<AnimationData> animationDataQueue, Function<GameObject, List<Attack>> createAttacks) {
        this.broadcastNPCUpdate = broadcastStateUpdate;
        this.npcSupplier = npcSupplier;

        this.disconnectGameobjectQueue = disconnectGameobjectQueue;
        this.attackQueue = attackQueue;
        this.animationDataQueue = animationDataQueue;
        this.createAttacks = createAttacks;
    }

    @Override
    public void update() {
        StateData.StateDataBuilder stateDataBuilder = StateData.builder();

        npcSupplier.get()
                .filter(npc -> 0 == r.nextInt(100) && npc.getState().equals("neutral"))
                .forEach(npc -> {
                    Direction direction = Direction.fromShort((short) r.nextInt(4));
                    Action move = new Move(npc, direction).perform();
                    move.getInvolved().forEach(stateDataBuilder::add);

                });

        npcSupplier.get().filter(npc -> npc.getState().equals("attack") && r.nextInt(10) == 0).forEach(npc -> {
            animationDataQueue.offer(new AnimationData(69420, npc.getName(), AnimationData.AnimationType.ATTACK));
            createAttacks.apply(npc).forEach(attack -> {
                attackQueue.offer(attack);
                attack.getInvolved().forEach(stateDataBuilder::add);
            });
        });

        if (stateDataBuilder.size() != 0){
            broadcastNPCUpdate.accept(stateDataBuilder.build());
        }


        npcSupplier.get().filter(npc -> npc.getCurrHp() <= 0).forEach(npc -> disconnectGameobjectQueue.offer(new DisconnectGameobject(npc.getName())));


    }
}
