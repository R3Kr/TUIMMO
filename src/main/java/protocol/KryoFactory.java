package protocol;

import com.esotericsoftware.kryo.Kryo;
import game.Direction;
import game.components.Player;
import protocol.data.*;

public class KryoFactory {
    public static void init(Kryo kryo){
        //kryo.register(Hello.class);
        //kryo.register(Test.class);

        kryo.register(Direction.class);
        kryo.register(Player.class);
        kryo.register(ConnectPlayer.class);
        kryo.register(DisconnectPlayer.class);
        kryo.register(AttackSignal.class);
        kryo.register(AnimationData.class);
        kryo.register(AnimationData.AnimationType.class);
        kryo.register(CoolSignal.class);
    }
}
