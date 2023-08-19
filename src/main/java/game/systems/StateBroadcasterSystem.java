package game.systems;

import game.components.Player;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StateBroadcasterSystem implements System{
    private Consumer<Map<String, Player>> broadcast;
    private Supplier<Map<String, Player>> positions;

    public StateBroadcasterSystem(Consumer<Map<String, Player>> broadcast, Supplier<Map<String, Player>> positions) {
        this.broadcast = broadcast;
        this.positions = positions;
    }

    @Override
    public void update() {
        broadcast.accept(positions.get());
    }
}
