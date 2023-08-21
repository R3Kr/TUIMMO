package game.systems;

import game.effects.Effect;

import java.util.Queue;
import java.util.TreeSet;

public class EffectSystem implements System{
    private Queue<Effect> effectQueue;
    private TreeSet<Effect> activeEffects = new TreeSet<>();

    public EffectSystem(Queue<Effect> effectQueue) {
        this.effectQueue = effectQueue;
    }

    @Override
    public void update() {
        while (!effectQueue.isEmpty()){
            activeEffects.add(effectQueue.poll());
        }

        for (Effect effect : activeEffects){
            effect.apply();
        }

        if (activeEffects.size() > 0 && !activeEffects.first().isActive()){
            Effect effect = activeEffects.pollFirst();
            assert effect != null;
            effect.deApply();
        }

    }
}
