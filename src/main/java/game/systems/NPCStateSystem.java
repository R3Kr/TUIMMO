package game.systems;

import game.Direction;
import game.Move;
import game.components.NPC;
import protocol.data.DisconnectGameobject;

import java.util.Queue;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NPCStateSystem implements System{
    private Runnable broadcastNPCUpdate;
    private Supplier<Stream<NPC>> npcSupplier;


    private Queue<DisconnectGameobject> disconnectGameobjectQueue;
    private Random r = new Random();

    public NPCStateSystem(Runnable broadcastStateUpdate, Supplier<Stream<NPC>> npcSupplier, Queue<DisconnectGameobject> disconnectGameobjectQueue) {
        this.broadcastNPCUpdate = broadcastStateUpdate;
        this.npcSupplier = npcSupplier;

        this.disconnectGameobjectQueue = disconnectGameobjectQueue;
    }

    @Override
    public void update() {


        npcSupplier.get()
                .filter(npc -> 0 == r.nextInt(100) && false)
                .peek(npc -> {
                    Direction direction = Direction.fromShort((short) r.nextInt(4));
                    new Move(npc, direction).perform();
                }).findFirst().ifPresent(npc -> broadcastNPCUpdate.run());

        npcSupplier.get().filter(npc -> npc.getCurrHp() <= 0).forEach(npc -> disconnectGameobjectQueue.offer(new DisconnectGameobject(npc.getName())));


    }
}
