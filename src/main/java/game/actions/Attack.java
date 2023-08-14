package game.actions;

import game.Combat;
import game.Player;

import java.util.Objects;
import java.util.Optional;

/**
 * The Attack class represents an action of attacking another player in the game.
 */
public class Attack implements Action {
    private static final int DMG = 5;
    private Combat attacker;
    private Optional<Combat> target;

    /**
     * Constructs a new Attack instance.
     *
     * @param attacker The player initiating the attack.
     * @param target   The player being targeted by the attack.
     */
    public Attack(Combat attacker, Combat target) {
        this.attacker = Objects.requireNonNull(attacker);
        this.target = Optional.ofNullable(target);
    }

    /**
     * Performs the attack action, reducing the target's current health points by the damage amount.
     *
     * @return The Attack instance after performing.
     */
    @Override
    public Action perform() {
        target.ifPresent(p -> p.setCurrHp(p.getCurrHp() - DMG));
        return this;
    }

    /**
     * Undoes the attack action, restoring the target's health points by the damage amount.
     *
     * @return The Attack instance after undoing.
     */
    @Override
    public Action undo() {
        target.ifPresent(p -> p.setCurrHp(p.getCurrHp() + DMG));
        return this;
    }
}
