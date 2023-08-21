package game;

import com.esotericsoftware.minlog.Log;
import game.components.Player;

/**
 * The Attack class represents an action of attacking another player in the game.
 */
public class Attack implements Action {
    private static final int DMG = 5;
    private Player attacker;
    private Player target;


    /**
     * Constructs a new Attack instance.
     *
     * @param attacker The player initiating the attack.
     * @param target   The player being targeted by the attack.
     */
    public Attack(Player attacker, Player target) {

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
        target.setCurrHp(target.getCurrHp()-5);
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
    public Player getPerformer() {
        return attacker;
    }

    public Player getTarget() {
        return target;
    }
}
