package game.actions;

import game.components.GameObject;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * The Action interface represents an action that can be performed and undone in the game.
 */
public interface Action extends Serializable {

    /**
     * Performs the action.
     *
     * @return The action instance after performing.
     */
    Action perform();

    /**
     * Undoes the action.
     *
     * @return The action instance after undoing.
     */
    Action undo();

    GameObject getPerformer();

    Stream<GameObject> getInvolved();
}
