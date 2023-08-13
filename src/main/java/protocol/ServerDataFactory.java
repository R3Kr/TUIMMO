package protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ServerDataFactory {
    public static Data createData(byte[] bytes) throws IOException {
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
            case STATEDATA -> {
                return new StateData(bytes);
            }
            case ANIMATIONDATA -> {
                return new AnimationData(bytes);
            }
            default -> throw new IllegalArgumentException("illegal packet datatype");
        }
    }
}
