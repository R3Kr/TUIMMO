package protocol.data;

import game.components.Player;

import java.util.Objects;

public class EntityPosition {
    public String entity;
    public Player player;

    public EntityPosition(String entity, Player player) {
        this.entity = entity;
        this.player = player;
    }

    public EntityPosition() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityPosition that = (EntityPosition) o;
        return Objects.equals(entity, that.entity) && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, player);
    }
}
