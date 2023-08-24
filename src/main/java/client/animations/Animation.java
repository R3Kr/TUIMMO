package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;
import game.components.GameObject;
import game.components.Player;

public abstract class Animation {
    protected int elapsed_frames = 0;
    private int total_frames;

    protected GameObject player;


    public Animation(GameObject player, int total_frames) {
        this.total_frames = total_frames;
        this.player = player;
    }

    public final void renderWith(TextGraphics tg){
        if (elapsed_frames++ >= total_frames){
            return;
        }
        _render(tg);
    }

    protected final boolean shouldDelete(){
        return elapsed_frames >= total_frames;
    }

    protected abstract void _render(TextGraphics tg);

    public GameObject getPerformer() {
        return player;
    }
}
