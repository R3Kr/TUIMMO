package protocol.data;

public class AnimationData {
    public enum AnimationType{
        ATTACK,
        COOL,
        BLOCK,
        REGEN;
    }
    public int performerId;
    public String player;
    public AnimationType type;


    public AnimationData() {
    }

    public AnimationData(int performerId, String player, AnimationType type) {
        this.performerId = performerId;
        this.player = player;
        this.type = type;
    }
}
