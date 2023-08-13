package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;

public class AnimationList extends ArrayList<Animation> {

    private TextGraphics tg;

    public AnimationList(TextGraphics tg) {
        this.tg = tg;
    }

    @Override
    public boolean add(Animation animation) {
        this.removeIf(Animation::shouldDelete);
        return super.add(animation);
    }

    public boolean add(AnimationBuilder builder){
        //player is already set
        Animation animation = builder.set(tg).build();
        return add(animation);
    }
}
