package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;
import game.Player;

public class AttackAnimation extends Animation{
    private static final int TOTAL_FRAMES = 10;

    public AttackAnimation(Player player) {
        super(TOTAL_FRAMES, player);
    }

    @Override
    protected void _render(TextGraphics tg) {
        if (elapsed_frames < 6) {
            tg.setCharacter(player.getX()-1, player.getY() -1, '°');
            tg.setCharacter(player.getX()+2, player.getY() -1, '°');
            tg.setCharacter(player.getX()+2, player.getY() +1, '°');
            tg.setCharacter(player.getX()-1, player.getY() +1, '°');
        }

        if (elapsed_frames > 3) {
            tg.setCharacter(player.getX()-1, player.getY(), '|');
            tg.setCharacter(player.getX(), player.getY() -1, '-');
            tg.setCharacter(player.getX()+1, player.getY() -1, '-');
            tg.setCharacter(player.getX()+2, player.getY(), '|');
            tg.setCharacter(player.getX(), player.getY() +1, '-');
            tg.setCharacter(player.getX() + 1, player.getY() +1, '-');
        }
    }
}
