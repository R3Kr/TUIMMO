package game;

import game.components.GameObject;
import game.components.Player;

import java.io.Serializable;

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
}
