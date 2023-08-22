package game.systems;

import game.components.GameObject;
import game.components.NPC;
import game.components.Player;

import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StateRecieverSystem implements System{


    private Queue<Player> playersToUpdate;
    private Queue<NPC> npcsToUpdate;
    private Supplier<Stream<GameObject>> gameObjects;

    private Consumer<Player> addPlayer;
    private Consumer<NPC> addNpc;


    public StateRecieverSystem(Queue<Player> playersToUpdate, Queue<NPC> npcsToUpdate, Supplier<Stream<GameObject>> gameObjects, Consumer<Player> addPlayer, Consumer<NPC> addNpc) {
        this.playersToUpdate = playersToUpdate;
        this.npcsToUpdate = npcsToUpdate;
        this.gameObjects = gameObjects;
        this.addPlayer = addPlayer;

        this.addNpc = addNpc;
    }

    @Override
    public void update() {
        while (!playersToUpdate.isEmpty()){
            Player player = playersToUpdate.poll();

            if (gameObjects.get().filter(p -> p instanceof Player).noneMatch(p -> p.getName().equals(player.getName()))){
                addPlayer.accept(player);
            }
            else {


                gameObjects.get().filter(p -> player.getName().equals(p.getName())).forEach(p -> ((Player)p).setData(player));
            }
        }

        while (!npcsToUpdate.isEmpty()){
            NPC npc = npcsToUpdate.poll();

            if (gameObjects.get().filter(p -> p instanceof NPC).noneMatch(p -> p.getName().equals(npc.getName()))){
                addNpc.accept(npc);
            }
            else {
                gameObjects.get().filter(p ->  npc.getName().equals(p.getName())).forEach(p -> ((NPC)p).setData(npc));
            }
        }

    }
}
