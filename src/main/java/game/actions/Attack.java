package game.actions;

import com.esotericsoftware.minlog.Log;
import game.actions.Action;
import game.components.GameObject;

import java.util.stream.Stream;

/**
 * The Attack class represents an action of attacking another player in the game.
 */
public class Attack implements Action {
    private static final int DMG = 5;
    private GameObject attacker;
    private GameObject target;

    /**
     * Constructs a new Attack instance.
     *
     * @param attacker The player initiating the attack.
     * @param target   The player being targeted by the attack.
     */
    public Attack(GameObject attacker, GameObject target) {

        this.attacker = attacker;
        this.target = target;
    }

    /**
     * Performs the attack action, reducing the target's current health points by the damage amount.
     *
     * @return The Attack instance after performing.
     */
    @Override
    public Action perform() {
        if (target.isInvincible()){
            return this;
        }
        target.takeDmg(DMG);
        Log.info(String.format("%s attacked %s for %d damage", attacker.getName(), target.getName(), DMG));
        Log.info(String.format("Attacker HP: %d ,  Target HP: %d", attacker.getCurrHp(), target.getCurrHp()));
        return this;
    }

    /**
     * Undoes the attack action, restoring the target's health points by the damage amount.
     *
     * @return The Attack instance after undoing.
     */
    @Override
    public Action undo() {
        target.setCurrHp(target.getCurrHp()+5);
        return this;
    }

    @Override
    public GameObject getPerformer() {
        return attacker;
    }

    public GameObject getTarget() {
        return target;
    }

    @Override
    public Stream<GameObject> getInvolved() {
        return Stream.of(getPerformer(), getTarget());
    }
}
