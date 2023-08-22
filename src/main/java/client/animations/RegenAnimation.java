package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;
import game.components.GameObject;
import game.components.Player;

public class RegenAnimation extends Animation {
    private static final int DURATION = 1000;

    public RegenAnimation(GameObject player) {
        super(player, DURATION);
    }

    @Override
    protected void _render(TextGraphics tg) {
        int frames = elapsed_frames % 14;

        if (frames < 5){
            tg.setCharacter(player.getX() - 1, player.getY() - 1, '°');
        }

        if (frames > 0 && frames < 6){
            tg.setCharacter(player.getX(), player.getY() - 1, '-');
        }

        if (frames > 1 && frames < 7){
            tg.setCharacter(player.getX() + 1, player.getY() - 1, '-');
        }

        if (frames > 2 && frames < 8){

            tg.setCharacter(player.getX() + 2, player.getY() - 1, '°');
        }

        if (frames > 3 && frames < 9){
            tg.setCharacter(player.getX() + 2, player.getY(), '|');
        }

        if (frames > 4 && frames < 10){

            tg.setCharacter(player.getX() + 2, player.getY() + 1, '°');
        }
        if (frames > 5 && frames < 11){
            tg.setCharacter(player.getX() + 1, player.getY() + 1, '-');

        }
        if (frames > 6 && frames < 12){
            tg.setCharacter(player.getX(), player.getY() + 1, '-');

        }

        if (frames > 7 && frames < 13){

            tg.setCharacter(player.getX() - 1, player.getY() + 1, '°');
        }

        if (frames > 8){

            tg.setCharacter(player.getX() - 1, player.getY(), '|');
        }





    }
}
