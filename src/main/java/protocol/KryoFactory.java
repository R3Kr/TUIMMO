package protocol;

import com.esotericsoftware.kryo.Kryo;
import game.actions.Direction;
import game.components.GameObject;
import game.components.NPC;
import game.components.Player;
import protocol.data.*;

public class KryoFactory {
    public static void init(Kryo kryo){
        //kryo.register(Hello.class);
        //kryo.register(Test.class);

        kryo.register(Direction.class);
        kryo.register(Player.class);
        kryo.register(ConnectPlayer.class);
        kryo.register(DisconnectGameobject.class);
        kryo.register(AttackSignal.class);
        kryo.register(AnimationData.class);
        kryo.register(AnimationData.AnimationType.class);
        kryo.register(CoolSignal.class);
        kryo.register(BlockSignal.class);
        kryo.register(NPC.class);
        kryo.register(RegenSignal.class);
        kryo.register(StateUpdateData.class);
        kryo.register(GameObject[].class);
        kryo.register(ChangeZoneSignal.class);

    }
}
