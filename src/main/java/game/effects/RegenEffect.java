package game.effects;

import game.components.Player;

public class RegenEffect extends Effect{
    private static final int DURATION = 1000;
    public RegenEffect(Player player) {
        super(DURATION, player);
    }

    @Override
    public void deApply() {

    }

    @Override
    protected void _apply() {
        if (getDurationLeft() % 5 == 0){
            player.setCurrHp(Math.min(player.getCurrHp() + 1, player.getMaxHp()));
        }
    }
}
