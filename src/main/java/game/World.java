package game;

import com.esotericsoftware.minlog.Log;
import game.components.Player;
import game.systems.System;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class World {
    //public record Entity(String id) {}
    private List<Player> players;
    public List<System> systems = new ArrayList<>();

    private ExecutorService threadPool;

    public World() {
        players = new ArrayList<>();
        threadPool = Executors.newSingleThreadExecutor();
    }

    public Player add(String name, int x, int y) {
        //Entity entity = new Entity(name);
        Player player = new Player(name, x, y, 100);

        players.add(player);
        return player;
    }

    public void remove(String name) {
        Optional<Player> player = players.stream().filter(p -> p.getName().equals(name)).findFirst();
        player.ifPresentOrElse(p -> players.remove(p), () -> Log.debug(String.format("Player %s doesnt exist", name)));
    }

    public <T> Stream<T> query(Class<T> tClass, Predicate<T> predicate) {

        return players.stream()
                .filter(tClass::isInstance)
                .map(tClass::cast)
                .filter(predicate);
        //test.run();
    }

    public Optional<Player> query(String name) {

        return players.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    public void tick() {
        systems.forEach(system -> threadPool.execute(system::update));
    }

    public World addSystem(System system) {
        systems.add(system);
        return this;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Attack> createAttacks(Player player) {

        ArrayList<Attack> attacks = new ArrayList<>();

        query(Player.class, p -> p != player && player.isCloseTo(p)).forEach(p -> attacks.add(new Attack(player, p)));

        return attacks;

    }


}
