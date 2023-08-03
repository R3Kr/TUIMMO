package server;

import game.Player;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameState extends ConcurrentHashMap<String, Player>{

    public AtomicBoolean sentStateUpdate;

    public GameState() {
        sentStateUpdate = new AtomicBoolean();
    }

    @Override
    public Player put(String key, Player value) {
        sentStateUpdate.set(false);
        return super.put(key, value);
    }

    public Player getMut(String key) {
        sentStateUpdate.set(false);
        return get(key);
    }
}
