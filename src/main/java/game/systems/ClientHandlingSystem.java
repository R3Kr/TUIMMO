package game.systems;

import game.Action;
import protocol.data.AnimationData;
import protocol.data.StateData;

import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientHandlingSystem implements System{
    private final Queue<Action> actionQueue;
    private final Queue<AnimationData> animationDataQueue;
    private final Consumer<StateData> broadcastStateToClients;

    private final BiConsumer<Integer, Object> broadcastToAllClientsExceptCaller;

    public ClientHandlingSystem(Queue<Action> actionQueue, Queue<AnimationData> animationDataQueue, Consumer<StateData> broadcastToClients, BiConsumer<Integer, Object> broadcastToAllClientsExceptCaller) {
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
            StateData.StateDataBuilder builder = StateData.builder();
            action.getInvolved().forEach(builder::add);
            broadcastStateToClients.accept(builder.build());
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
