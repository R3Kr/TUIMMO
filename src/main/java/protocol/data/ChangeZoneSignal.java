package protocol.data;

import game.actions.Direction;

public class ChangeZoneSignal {
    public int newZoneID;
    public Direction incomingDirection;

    public ChangeZoneSignal() {
    }

    public ChangeZoneSignal(int newZoneID, Direction incomingDirection) {
        this.newZoneID = newZoneID;
        this.incomingDirection = incomingDirection;
    }
}
