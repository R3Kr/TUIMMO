package protocol;

import game.Player;

public interface ActionData extends Data{
    String getPlayerName();

    DataType getDataType();
}
