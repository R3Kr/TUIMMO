package game.actions;

import game.Player;

import java.util.Objects;

public class Attack implements Action{
    private static final int DMG = 5;
    private Player attacker;
    private Player target;

    public Attack(Player attacker, Player target) {
        this.attacker = Objects.requireNonNull(attacker);
        this.target = Objects.requireNonNull(target);
    }


    @Override
    public Action perform() {
        target.setCurrHp(target.getCurrHp() - DMG);
        return this;
    }

    @Override
    public Action undo() {
        target.setCurrHp(target.getCurrHp() + DMG);
        return this;
    }
}
