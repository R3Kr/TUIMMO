package game.systems;

import protocol.data.DisconnectGameobject;

import java.util.Queue;
import java.util.function.Consumer;

public class ObjectRemovalSystem implements System{
    private Queue<DisconnectGameobject> removeQueue;
    private Consumer<DisconnectGameobject> removeObject;




    public ObjectRemovalSystem(Queue<DisconnectGameobject> removeQueue, Consumer<DisconnectGameobject> removeObject) {
        this.removeQueue = removeQueue;
        this.removeObject = removeObject;
    }

    @Override
    public void update() {
        while (!removeQueue.isEmpty()){
            DisconnectGameobject disconnectGameobject = removeQueue.poll();
            removeObject.accept(disconnectGameobject);
        }
    }
}
