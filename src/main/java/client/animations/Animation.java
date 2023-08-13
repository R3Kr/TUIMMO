package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import game.Player;

public abstract class Animation {
    protected int elapsed_frames = 0;
    private int total_frames;

    protected Player player;

    protected TextGraphics tg;

    public Animation(int total_frames, Player player, TextGraphics tg) {
        this.total_frames = total_frames;
        this.player = player;
        this.tg = tg;
    }

    public final void render(){
        if (elapsed_frames++ >= total_frames){
            return;
        }
        _render();
    }

    protected final boolean shouldDelete(){
        return elapsed_frames >= total_frames;
    }

    protected abstract void _render();


}
