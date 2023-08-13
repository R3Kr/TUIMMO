package client.animations;

import java.util.ArrayList;

public class AnimationList extends ArrayList<Animation> {
    @Override
    public boolean add(Animation animation) {
        this.removeIf(Animation::shouldDelete);
        return super.add(animation);
    }
}
