package game.systems;

import game.components.NPC;

import java.util.Random;
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
                .filter(npc -> npc.hashCode() % 20 == r.nextInt(21))
                .forEach(npc -> {
            npc.setX(r.nextInt(79));
            npc.setY(r.nextInt(26));
        });
        broadcastNPCUpdate.run();


    }
}
