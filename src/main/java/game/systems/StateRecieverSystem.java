package game.systems;

import game.components.GameObject;
import game.components.NPC;
import game.components.Player;

import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class StateRecieverSystem implements System{


    private Queue<Player> playersToAdd;
    private Queue<NPC> npcsToAdd;

    private Queue<GameObject> gameObjectsToUpdate;
    private Supplier<Stream<GameObject>> gameObjects;

    private Consumer<GameObject> addGameObject;


    public StateRecieverSystem(Queue<Player> playersToUpdate, Queue<NPC> npcsToUpdate, Queue<GameObject> gameObjectsToAdd, Supplier<Stream<GameObject>> gameObjects, Consumer<GameObject> addGameObject) {
        this.playersToAdd = playersToUpdate;
        this.npcsToAdd = npcsToUpdate;
        this.gameObjectsToUpdate = gameObjectsToAdd;
        this.gameObjects = gameObjects;
        this.addGameObject = addGameObject;

    }

    @Override
    public void update() {

        while (!gameObjectsToUpdate.isEmpty()){
            GameObject gameObject = gameObjectsToUpdate.poll();
            gameObjects.get().filter(p -> gameObject.getName().equals(p.getName())).forEach(p -> p.setData(gameObject));
        }


        while (!playersToAdd.isEmpty()){
            Player player = playersToAdd.poll();
            addGameObject.accept(player);

        }

        while (!npcsToAdd.isEmpty()){
            NPC npc = npcsToAdd.poll();

            addGameObject.accept(npc);


        }

    }
}
