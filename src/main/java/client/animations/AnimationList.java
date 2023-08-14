package client.animations;

import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimationList extends CopyOnWriteArrayList<Animation> {



    @Override
    public boolean add(Animation animation) {
        this.removeIf(Animation::shouldDelete);
        return super.add(animation);
    }

//    public boolean add(AnimationBuilder builder){
//        //player is already set
//        Animation animation = builder.set(tg).build();
//        return add(animation);
//    }
}
