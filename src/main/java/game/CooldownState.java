package game;

import java.util.function.ObjLongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class CooldownState implements Predicate<Long> {

    private long lastPressed = 0;
    private final int cooldown;

    public CooldownState(int cooldown) {
        this.cooldown = cooldown;
    }



    public void set(long lastPressed){
        this.lastPressed = lastPressed;
    }

    @Override
    public boolean test(Long time) {
        return time - lastPressed < cooldown;
    }
}
