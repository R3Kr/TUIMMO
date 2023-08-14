package protocol;

import game.Player;
import game.actions.Action;
import game.actions.Attack;
import game.actions.Move;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * The ActionDataFactory class provides methods to create and handle action-related data.
 */
public class ActionDataFactory {
    /**
     * Creates an instance of ActionData based on the provided byte array.
     *
     * @param bytes The byte array containing the action data.
     * @return An instance of ActionData representing the provided data.
     * @throws IOException If an I/O error occurs while processing the data.
     */
    public static ActionData createData(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        DataType dataType = DataType.fromShort(dis.readShort()); // id already known
        dis.close();

        switch (dataType) {
            case MOVEDATA -> {
                return new MoveData(bytes);
            }
            case ATTACKDATA -> {
                return new AttackData(bytes);
            }
            case COOLDATA -> {

                return new CoolData(bytes);
            }
            default -> throw new IllegalArgumentException("illegal packet datatype");
        }
    }

    /**
     * Creates an Action instance based on the provided data and player information.
     *
     * @param players The map of player names to player instances.
     * @param data    The action data.
     * @return An optional Action instance, or empty if the action cannot be created.
     */
    public static Optional<Action> createAction(Map<String, Player> players, ActionData data) {
        Player player = players.get(data.getPlayerName());
        if (player == null) {
            return Optional.empty();
        }

        DataType dataType = data.getDataType();

        switch (dataType) {
            case MOVEDATA -> {
                return Optional.of(new Move(player, ((MoveData) data).getDirection()));
            }
            case ATTACKDATA -> {

                return Optional.of(new Attack(player, players.get(((AttackData) data).getTargetName())));
            }
            default -> {
                return Optional.empty();
            }
        }

    }
}
