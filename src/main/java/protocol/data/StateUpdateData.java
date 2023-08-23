package protocol.data;

import game.components.GameObject;

import java.util.*;

public class StateUpdateData {
    public static class StateDataBuilder{
        Collection<GameObject> gameObjectsToUpdate;

        private StateDataBuilder(List<GameObject> gameObjectsToUpdate) {
            this.gameObjectsToUpdate = gameObjectsToUpdate;
        }

        private StateDataBuilder() {
            gameObjectsToUpdate = new HashSet<>();
        }

        public StateDataBuilder add(GameObject object){
            gameObjectsToUpdate.add(object);
            return this;
        }

        public StateUpdateData build(){
            return new StateUpdateData(gameObjectsToUpdate);
        }

        public int size(){
            return gameObjectsToUpdate.size();
        }

        public void clear(){
            gameObjectsToUpdate.clear();
        }
    }
    public static StateDataBuilder builder(){
        return new StateDataBuilder();
    }
    public static StateDataBuilder builder(List<GameObject> gameObjects){
        return new StateDataBuilder(gameObjects);
    }

    public GameObject[] gameObjects;

    private StateUpdateData(Collection<GameObject> gameObjects){
        this.gameObjects = gameObjects.toArray(new GameObject[0]);
    }

    private StateUpdateData() {
    }
}
