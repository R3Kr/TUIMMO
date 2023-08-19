package game.systems;

import game.Action;
import protocol.data.AnimationData;

import java.util.Queue;
import java.util.function.BiConsumer;

public class ClientHandlingSystem implements System{
    private final Queue<Action> actionQueue;
    private final Queue<AnimationData> animationDataQueue;
    private final Runnable broadcastStateToClients;

    private final BiConsumer<Integer, Object> broadcastToAllClientsExceptCaller;

    public ClientHandlingSystem(Queue<Action> actionQueue, Queue<AnimationData> animationDataQueue, Runnable broadcastToClients, BiConsumer<Integer, Object> broadcastToAllClientsExceptCaller) {
        this.actionQueue = actionQueue;
        this.animationDataQueue = animationDataQueue;
        this.broadcastStateToClients = broadcastToClients;
        this.broadcastToAllClientsExceptCaller = broadcastToAllClientsExceptCaller;
    }

    @Override
    public void update() {
        while (!actionQueue.isEmpty()){
            Action action = actionQueue.poll();
            action.perform();
            broadcastStateToClients.run();
        }

        while (!animationDataQueue.isEmpty()){
            AnimationData animationData = animationDataQueue.poll();
            broadcastToAllClientsExceptCaller.accept(animationData.performerId, animationData);
        }
    }

//    private Action getAction(ActionData actionData){
//       // Position player = getPlayer.apply(actionData.player);
//        //java.lang.System.out.println("player moving");
//        return new Move(player, actionData.direction);
//    }
}
