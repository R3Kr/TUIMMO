package server;

import game.Entity;
import game.Player;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The GameState class represents the state of the game, including player information.
 */
public class GameState extends ConcurrentHashMap<String, Entity> {

    /**
     * A flag to indicate whether a state update has been sent.
     */
    public AtomicBoolean sentStateUpdate;

    /**
     * Constructs a GameState object and initializes the sentStateUpdate flag.
     */
    public GameState() {
        sentStateUpdate = new AtomicBoolean();
    }

    /**
     * Overrides the put method to set the sentStateUpdate flag to false before adding a player.
     *
     * @param key   The player's key.
     * @param value The player object.
     * @return The previously mapped player object with the specified key, or null if there was no previous mapping.
     */
    @Override
    public Entity put(String key, Entity value) {
        sentStateUpdate.set(false);
        return super.put(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        sentStateUpdate.set(false);
        return super.remove(key, value);
    }

    @Override
    public Entity putIfAbsent(String key, Entity value) {
        Entity player = super.putIfAbsent(key, value);
        if (player == null){
            sentStateUpdate.set(false);
        }
        return player;
    }

    /**
     * Retrieves a player object associated with the specified key and sets the sentStateUpdate flag to false.
     *
     * @param key The key of the player to be retrieved.
     * @return The player object associated with the key, or null if the key is not present.
     */
    public Player getPlayerMut(String key) {
        Optional<Entry<String, Entity>> player = this.entrySet().stream().filter(e -> e.getValue() instanceof Player && e.getKey().equals(key)).findFirst();
        sentStateUpdate.set(false);
        return (Player) player.orElseThrow().getValue();
    }
}
