package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;

public class CoolAnimation extends Animation{

    private final int x = 0;
    private final int y = 0;
    public CoolAnimation() {
        super(1000);
//        x = player.getX();
//        y = player.getY();
    }

    @Override
    protected void _render(TextGraphics tg) {
        switch (elapsed_frames % 7){
            case 0 -> tg.setCharacter(x, y, '@');
            case 1 -> tg.setCharacter(x, y, '?');
            case 2 -> tg.setCharacter(x, y, '#');
            case 3 -> tg.setCharacter(x, y, '%');
            case 4 -> tg.setCharacter(x, y, '&');
            case 5 -> tg.setCharacter(x, y, '!');
            case 6 -> tg.setCharacter(x, y, '€');

        }
    }
}
