package game;

import java.io.Serializable;

/**
 * The Movable interface represents an object that can be moved in different directions.
 */
public interface Movable extends Serializable {

    /**
     * Moves the object in the specified direction.
     *
     * @param direction The direction in which to move the object.
     */
    void move(Direction direction);
}
