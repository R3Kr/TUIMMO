package game.systems;

import game.Direction;
import game.Move;
import game.components.NPC;

import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NPCStateSystem implements System{
    private Runnable broadcastNPCUpdate;
    private Supplier<Stream<NPC>> npcSupplier;
    private Random r = new Random();

    public NPCStateSystem(Runnable broadcastStateUpdate, Supplier<Stream<NPC>> npcSupplier) {
        this.broadcastNPCUpdate = broadcastStateUpdate;
        this.npcSupplier = npcSupplier;
    }

    @Override
    public void update() {


        npcSupplier.get()
                .filter(npc -> 0 == r.nextInt(100))
                .peek(npc -> {
                    Direction direction = Direction.fromShort((short) r.nextInt(4));
                    new Move(npc, direction).perform();
                }).findFirst().ifPresent(npc -> broadcastNPCUpdate.run());



    }
}
