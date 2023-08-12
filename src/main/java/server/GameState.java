package server;

import game.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The GameState class represents the state of the game, including player information.
 */
public class GameState extends ConcurrentHashMap<String, Player> {

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
    public Player put(String key, Player value) {
        sentStateUpdate.set(false);
        return super.put(key, value);
    }

    /**
     * Retrieves a player object associated with the specified key and sets the sentStateUpdate flag to false.
     *
     * @param key The key of the player to be retrieved.
     * @return The player object associated with the key, or null if the key is not present.
     */
    public Player getMut(String key) {
        sentStateUpdate.set(false);
        return get(key);
    }
}
