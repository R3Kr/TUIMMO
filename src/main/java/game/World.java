package game;

import com.esotericsoftware.minlog.Log;
import game.actions.Attack;
import game.components.GameObject;
import game.components.NPC;
import game.components.Player;
import game.systems.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class World {

    //public record Entity(String id) {}
    private List<GameObject> gameObjects;

    public List<System> systems = new ArrayList<>();

    private ExecutorService threadPool;

    public World() {
        gameObjects = new CopyOnWriteArrayList<>();
        threadPool = Executors.newSingleThreadExecutor();
    }

    public Player addPlayer(String name, int x, int y) {
        //Entity entity = new Entity(name);
        Player player = new Player(name, x, y, 100);

        gameObjects.add(player);
        return player;
    }


    public NPC addNPC(String name, int x, int y) {
        //Entity entity = new Entity(name);
        NPC player = new NPC(name, x, y);

        gameObjects.add(player);
        return player;
    }

    public void add(GameObject gameObject){
        gameObjects.add(gameObject);
    }

    public void remove(String name) {
        Optional<GameObject> player = gameObjects.stream().filter(p -> p.getName().equals(name)).findFirst();
        player.ifPresentOrElse(p -> gameObjects.remove(p), () -> Log.info(String.format("Player %s doesnt exist", name)));
    }

    public <T extends GameObject> Stream<T> query(Class<T> tClass, Predicate<T> predicate) {


        return gameObjects.stream()
                .filter(tClass::isInstance)
                .map(tClass::cast)
                .filter(predicate);


        //test.run();
    }

    public Stream<GameObject> query(Predicate<GameObject> predicate){
        return gameObjects.stream().filter(predicate);
    }

    public Optional<GameObject> query(String name){
        return gameObjects.stream().filter(o -> o.getName().equals(name)).findFirst();
    }

    public Optional<Player> queryPlayer(String name) {

        return query(Player.class, player -> player.getName().equals(name)).findFirst();
    }

    public void tick() {
        systems.forEach(system -> threadPool.execute(system::update));
    }

    public World addSystem(System system) {
        systems.add(system);
        return this;
    }



    public List<Attack> createAttacks(GameObject player) {

        ArrayList<Attack> attacks = new ArrayList<>();

        query(p -> p != player && player.isCloseTo(p)).forEach(p -> attacks.add(new Attack(player, p)));

        return attacks;

    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }
}
