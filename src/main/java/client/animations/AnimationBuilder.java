package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;
import game.Player;

public class AnimationBuilder {
    private TextGraphics tg;
    private Player player;

    public AnimationBuilder() {
    }

    public AnimationBuilder set(TextGraphics tg){
        this.tg = tg;
        return this;
    }

    public AnimationBuilder set(Player player){
        this.player = player;
        return this;
    }

    public Animation build(){
        return new AttackAnimation(player, tg);
    }
}
