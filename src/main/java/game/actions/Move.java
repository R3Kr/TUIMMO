package game.actions;

import game.Direction;
import game.Movable;

import java.util.Objects;

public class Move implements Action{
    private Movable movable;
    private Direction direction;

    public Move(Movable movable, Direction direction){
        this.movable = Objects.requireNonNull(movable);
        this.direction = Objects.requireNonNull(direction);
    }

    @Override
    public Action perform() {
        movable.move(direction);
        return this;
    }

    @Override
    public Action undo() {
        switch (direction){
            case UP -> movable.move(Direction.DOWN);
            case DOWN -> movable.move(Direction.UP);
            case RIGHT -> movable.move(Direction.LEFT);
            case LEFT -> movable.move(Direction.RIGHT);
        }
        return this;
    }
}
