package game.actions;

import java.util.Objects;

/**
 * The Move class represents an action of moving a movable object in a specified direction.
 */
public class Move implements Action {
    private Direction direction;

    /**
     * Constructs a new Move instance.
     *
     * @param movable   The movable object to be moved.
     * @param direction The direction in which to move the object.
     */
    public Move(Direction direction) {

        this.direction = Objects.requireNonNull(direction);
    }

    /**
     * Performs the move action, moving the movable object in the specified direction.
     *
     * @return The Move instance after performing.
     */
    @Override
    public Action perform() {
        //movable.move(direction);
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
}
