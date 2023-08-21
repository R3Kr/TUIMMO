package game.effects;

import game.components.Player;

public abstract class Effect implements Comparable<Effect>{

    protected final Player player;
    private Integer duration;

    public Effect(int duration, Player player) {

        this.player = player;
        this.duration = duration;
    }

    public void apply(){
        if (isActive()){

            _apply();
        }
        duration--;
    }

    public abstract void deApply();

    protected abstract void _apply();
    public boolean isActive(){
        return duration > 0;
    }

    @Override
    public int compareTo(Effect o) {
        return duration.compareTo(o.duration);
    }


}
