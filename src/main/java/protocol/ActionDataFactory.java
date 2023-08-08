package protocol;

import game.Player;
import game.actions.Action;
import game.actions.Attack;
import game.actions.Move;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

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

    public static Action createAction(Map<String, Player> players, ActionData data){
        Player player = players.get(data.getPlayerName());
        DataType dataType = data.getDataType();

        switch (dataType){
            case MOVEDATA -> {
                return new Move(player, ((MoveData) data).getDirection());
            }
            case ATTACKDATA -> {
                return new Attack(player, players.get(((AttackData) data).getTargetName()));
            }
            default -> throw new IllegalArgumentException("illegal datya");
        }

    }
}
