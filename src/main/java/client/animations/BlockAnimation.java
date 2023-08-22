package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;
import game.components.GameObject;
import game.components.Player;

public class BlockAnimation extends Animation{
    private static final int FRAMES = 100;
    public BlockAnimation(GameObject player) {
        super(player, FRAMES);
    }

    @Override
    protected void _render(TextGraphics tg) {
        if (elapsed_frames < 90) {

            tg.setCharacter(player.getX()-1, player.getY() -1, '°');
            tg.setCharacter(player.getX()+2, player.getY() -1, '°');
            tg.setCharacter(player.getX()+2, player.getY() +1, '°');
            tg.setCharacter(player.getX()-1, player.getY() +1, '°');
        }

        if (elapsed_frames > 10) {
            tg.setCharacter(player.getX()-1, player.getY(), '|');
            tg.setCharacter(player.getX(), player.getY() -1, '-');
            tg.setCharacter(player.getX()+1, player.getY() -1, '-');
            tg.setCharacter(player.getX()+2, player.getY(), '|');
            tg.setCharacter(player.getX(), player.getY() +1, '-');
            tg.setCharacter(player.getX() + 1, player.getY() +1, '-');
        }

    }
}
