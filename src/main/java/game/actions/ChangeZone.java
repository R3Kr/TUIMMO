package game.actions;

import game.components.GameObject;

import java.util.Optional;
import java.util.stream.Stream;

public class ChangeZone implements Action{

    private GameObject performer;
    private int newZoneID;

    private Direction incomingDirection;

    private Optional<Integer> previousZoneID = Optional.empty();

    public ChangeZone(GameObject performer, int newZoneID, Direction incomingDirection) {
        this.performer = performer;
        this.newZoneID = newZoneID;
        this.incomingDirection = incomingDirection;
    }

    @Override
    public Action perform() {
        previousZoneID = Optional.of(performer.getZoneID());
        performer.setZoneID(newZoneID);
        switch (incomingDirection){
            case UP -> {
                performer.setX(38);
                performer.setY(23);
            }
            case DOWN -> {
                performer.setX(38);
                performer.setY(1);
            }
            case RIGHT -> {
                performer.setX(2);
                performer.setY(12);
            }
            case LEFT -> {
                performer.setX(76);
                performer.setY(12);
            }
        }
        return this;
    }

    @Override
    public Action undo() {
        previousZoneID.ifPresent(id -> performer.setZoneID(id));
        return this;
    }

    @Override
    public GameObject getPerformer() {
        return performer;
    }

    @Override
    public Stream<GameObject> getInvolved() {
        return Stream.of(performer);
    }
}
