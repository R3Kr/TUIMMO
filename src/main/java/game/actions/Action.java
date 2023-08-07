package game.actions;

import java.io.Serializable;

public interface Action extends Serializable {
    Action perform();

    Action undo();
}
