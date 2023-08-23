package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.*;
import game.actions.Action;
import game.actions.ChangeZone;
import game.actions.Direction;
import game.actions.Move;
import game.components.Player;
import game.effects.BlockEffect;
import game.effects.Effect;
import game.effects.RegenEffect;
import protocol.data.*;

import java.util.Queue;

public class ClientListener extends Listener {

    private Queue<Action> actionDataQueue;
    private Queue<ConnectPlayer> playersToConnect;
    private Queue<String> playersToDisconnect;

    private Queue<AnimationData> animationDataQueue;

    private Queue<Effect> effectQueue;

    private World world;

    private String name;

    public ClientListener(Queue<Action> actionDataQueue, Queue<ConnectPlayer> playersToConnect, Queue<String> playersToDisconnect, Queue<AnimationData> animationDataQueue, Queue<Effect> effectQueue, World world) {
        this.actionDataQueue = actionDataQueue;
        this.playersToConnect = playersToConnect;
        this.playersToDisconnect = playersToDisconnect;
        this.animationDataQueue = animationDataQueue;
        this.effectQueue = effectQueue;
        this.world = world;
    }


    @Override
    public void received(Connection connection, Object object) {

        if (object instanceof Direction){
            Player player = world.queryPlayer(name).orElseThrow();
            actionDataQueue.offer(new Move(player, (Direction) object));
        } else if (object instanceof ConnectPlayer) {
            name = ((ConnectPlayer) object).player;
            ((ConnectPlayer) object).connectionId = connection.getID();
            playersToConnect.offer(((ConnectPlayer) object));

        } else if (object instanceof DisconnectGameobject) {
            playersToDisconnect.offer(((DisconnectGameobject) object).player);
        }
        else if (object instanceof AttackSignal){
            Player attacker = world.queryPlayer(name).orElseThrow();
            animationDataQueue.offer(new AnimationData(connection.getID(), name, AnimationData.AnimationType.ATTACK));
            world.createAttacks(attacker).forEach(attack -> actionDataQueue.offer(attack));
        } else if (object instanceof CoolSignal) {
            animationDataQueue.offer(new AnimationData(connection.getID(), name, AnimationData.AnimationType.COOL));
        } else if (object instanceof BlockSignal) {
            animationDataQueue.offer(new AnimationData(connection.getID(), name, AnimationData.AnimationType.BLOCK));
            Player blocker = world.queryPlayer(name).orElseThrow();
            effectQueue.offer(new BlockEffect(blocker));
        }
        else if (object instanceof RegenSignal){
            Player player = world.queryPlayer(name).orElseThrow();
            effectQueue.offer(new RegenEffect(player));
            animationDataQueue.offer(new AnimationData(connection.getID(), name, AnimationData.AnimationType.REGEN));
        }
        else if (object instanceof ChangeZoneSignal){
            Player player = world.queryPlayer(name).orElseThrow();
            actionDataQueue.offer(new ChangeZone(player, ((ChangeZoneSignal) object).newZoneID, ((ChangeZoneSignal) object).incomingDirection));
        }
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        playersToDisconnect.offer(name);
        //Log.info(String.format("%s disconnected", name));
    }
}
