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

public class ActionDataFactory {
    public static ActionData createData(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        DataType dataType = DataType.fromShort(dis.readShort()); //id already known
        dis.close();

        switch (dataType){
            case MOVEDATA -> {
                return new MoveData(bytes);
            }
            case ATTACKDATA -> {
                return new AttackData(bytes);
            }
            default -> throw new IllegalArgumentException("illegal packet datatype");
        }
    }

    public static Optional<Action> createAction(Map<String, Player> players, ActionData data){
        Player player = players.get(data.getPlayerName());
        if (player == null){
            return Optional.empty();
        }

        DataType dataType = data.getDataType();

        switch (dataType){
            case MOVEDATA -> {
                return Optional.of(new Move(player, ((MoveData) data).getDirection()));
            }
            case ATTACKDATA -> {
                Player player2 = players.get(((AttackData) data).getTargetName());
                if (player2 == null){
                    return Optional.empty();
                }
                return Optional.of(new Attack(player, players.get(((AttackData) data).getTargetName())));
            }
            default -> throw new IllegalArgumentException("illegal datya");
        }

    }
}
