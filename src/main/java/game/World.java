package game;

import com.esotericsoftware.minlog.Log;
import game.components.GameObject;
import game.components.NPC;
import game.components.Player;
import game.systems.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        gameObjects = new ArrayList<>();
        threadPool = Executors.newSingleThreadExecutor();
    }

    public Player addPlayer(String name, int x, int y) {
        //Entity entity = new Entity(name);
        Player player = new Player(name, x, y, 100);

        gameObjects.add(player);
        return player;
    }

    public Player addPlayer(Player player){
        gameObjects.add(player);
        return player;
    }
    public NPC addNPC(String name, int x, int y) {
        //Entity entity = new Entity(name);
        NPC player = new NPC(name, x, y);

        gameObjects.add(player);
        return player;
    }

    public NPC addNPC(NPC npc){
        gameObjects.add(npc);
        return npc;
    }

    public void remove(String name) {
        Optional<GameObject> player = gameObjects.stream().filter(p -> p.getName().equals(name)).findFirst();
        player.ifPresentOrElse(p -> gameObjects.remove(p), () -> Log.debug(String.format("Player %s doesnt exist", name)));
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

    public Optional<Player> query(String name) {

        return query(Player.class, player -> player.getName().equals(name)).findFirst();
    }

    public void tick() {
        systems.forEach(system -> threadPool.execute(system::update));
    }

    public World addSystem(System system) {
        systems.add(system);
        return this;
    }



    public List<Attack> createAttacks(Player player) {

        ArrayList<Attack> attacks = new ArrayList<>();

        query(Player.class, p -> p != player && player.isCloseTo(p)).forEach(p -> attacks.add(new Attack(player, p)));

        return attacks;

    }


}
