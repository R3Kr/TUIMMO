package server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.*;
import game.components.Player;
import protocol.data.*;

import java.util.Queue;

public class ClientListener extends Listener {

    private Queue<Action> actionDataQueue;
    private Queue<String> playersToConnect;
    private Queue<String> playersToDisconnect;

    private Queue<AnimationData> animationDataQueue;

    private World world;

    private String name;

    public ClientListener(Queue<Action> actionDataQueue, Queue<String> playersToConnect, Queue<String> playersToDisconnect, Queue<AnimationData> animationDataQueue, World world) {
        this.actionDataQueue = actionDataQueue;
        this.playersToConnect = playersToConnect;
        this.playersToDisconnect = playersToDisconnect;
        this.animationDataQueue = animationDataQueue;
        this.world = world;
    }


    @Override
    public void received(Connection connection, Object object) {

        if (object instanceof Direction){
            Player player = world.query(name).orElseThrow();
            actionDataQueue.offer(new Move(player, (Direction) object));
        } else if (object instanceof ConnectPlayer) {
            name = ((ConnectPlayer) object).player;
            playersToConnect.offer(name);

        } else if (object instanceof DisconnectPlayer) {
            playersToDisconnect.offer(((DisconnectPlayer) object).player);
        }
        else if (object instanceof AttackSignal){
            Player attacker = world.query(name).orElseThrow();
            animationDataQueue.offer(new AnimationData(connection.getID(), name, AnimationData.AnimationType.ATTACK));
            world.createAttacks(attacker).forEach(attack -> actionDataQueue.offer(attack));
        } else if (object instanceof CoolSignal) {
            animationDataQueue.offer(new AnimationData(connection.getID(), name, AnimationData.AnimationType.COOL));
        }
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        playersToDisconnect.offer(name);
        //Log.info(String.format("%s disconnected", name));
    }
}
