package protocol;

import game.Player;

/**
 * The ActionData interface represents data related to an action performed in the game.
 */
public interface ActionData extends Data {
    /**
     * Gets the name of the player associated with the action.
     *
     * @return The name of the player.
     */
    String getPlayerName();

    /**
     * Gets the data type of the action.
     *
     * @return The data type of the action.
     */
    DataType getDataType();
}
