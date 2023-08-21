package game.effects;

import game.components.Player;

public class BlockEffect extends Effect {
    private static final int DURATION = 100;

    public BlockEffect(Player player) {
        super(DURATION, player);
    }

    @Override
    protected void _apply() {
        player.setInvincible(true);
    }

    @Override
    public void deApply() {
        player.setInvincible(false);
    }
}
