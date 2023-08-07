package game;

import java.io.Serializable;

public interface Movable extends Serializable {
    void move(Direction direction);
}
