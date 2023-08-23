package game.systems;

import game.actions.Action;
import game.components.GameObject;
import protocol.data.AnimationData;
import protocol.data.StateUpdateData;

import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ClientHandlingSystem implements System{
    private final Queue<Action> actionQueue;
    private final Queue<AnimationData> animationDataQueue;

    private final Queue<GameObject> objectsToUpdate;
    private final Consumer<StateUpdateData> broadcastStateToClients;

    private final BiConsumer<Integer, Object> broadcastToAllClientsExceptCaller;

    private StateUpdateData.StateDataBuilder stateDataBuilder = StateUpdateData.builder();

    public ClientHandlingSystem(Queue<Action> actionQueue, Queue<AnimationData> animationDataQueue, Queue<GameObject> objectsToUpdate, Consumer<StateUpdateData> broadcastToClients, BiConsumer<Integer, Object> broadcastToAllClientsExceptCaller) {
        this.actionQueue = actionQueue;
        this.animationDataQueue = animationDataQueue;
        this.objectsToUpdate = objectsToUpdate;
        this.broadcastStateToClients = broadcastToClients;
        this.broadcastToAllClientsExceptCaller = broadcastToAllClientsExceptCaller;
    }

    @Override
    public void update() {
        while (!actionQueue.isEmpty()){
            Action action = actionQueue.poll();
            action.perform();
            //StateUpdateData.StateDataBuilder builder = StateUpdateData.builder();
            action.getInvolved().forEach(objectsToUpdate::add);
            //broadcastStateToClients.accept(builder.build());
        }

        while (!animationDataQueue.isEmpty()){
            AnimationData animationData = animationDataQueue.poll();
            broadcastToAllClientsExceptCaller.accept(animationData.performerId, animationData);
        }

        int counter = 0;
        while (!objectsToUpdate.isEmpty()){

            if (counter == 0){
                stateDataBuilder.clear();
            }
            stateDataBuilder.add(objectsToUpdate.poll());
            counter++;
        }
        if (counter != 0){
            broadcastStateToClients.accept(stateDataBuilder.build());
        }

    }

//    private Action getAction(ActionData actionData){
//       // Position player = getPlayer.apply(actionData.player);
//        //java.lang.System.out.println("player moving");
//        return new Move(player, actionData.direction);
//    }
}
