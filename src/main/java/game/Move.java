package game;

import game.components.Component;
import game.components.Player;

import java.util.Objects;

/**
 * The Move class represents an action of moving a movable object in a specified direction.
 */
public class Move implements Action {
    private Player player;
    private Direction direction;

    /**
     * Constructs a new Move instance.
     *
     * @param movable   The movable object to be moved.
     * @param direction The direction in which to move the object.
     */
    public Move(Player player, Direction direction) {
        this.player = Objects.requireNonNull(player);
        this.direction = Objects.requireNonNull(direction);
    }

    /**
     * Performs the move action, moving the movable object in the specified direction.
     *
     * @return The Move instance after performing.
     */
    @Override
    public Action perform() {
        switch (direction){
            case UP -> player.setY(Math.max(player.getY() - 1, 0));
            case DOWN -> player.setY(Math.min(player.getY() + 1, 25));
            case LEFT -> player.setX(Math.max(player.getX() - 2, 0));
            case RIGHT -> player.setX(Math.min(player.getX() + 2, 78));
        }
        return this;
    }

    /**
     * Undoes the move action by moving the movable object in the opposite direction.
     *
     * @return The Move instance after undoing.
     */
    @Override
    public Action undo() {
//        switch (direction) {
//            case UP -> movable.move(Direction.DOWN);
//            case DOWN -> movable.move(Direction.UP);
//            case RIGHT -> movable.move(Direction.LEFT);
//            case LEFT -> movable.move(Direction.RIGHT);
//        }
        return this;
    }

    @Override
    public Player getPerformer() {
        return player;
    }
}