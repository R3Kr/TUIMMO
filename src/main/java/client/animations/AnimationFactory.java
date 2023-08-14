package client.animations;

import game.Player;
import protocol.AnimationData;

import java.util.Map;
import java.util.Optional;

public class AnimationFactory {
    public static Optional<Animation> createAnimation(Map<String, Player> players, AnimationData data){
        Player player = players.get(data.getPlayer());
        Animation animation;
        if (player == null){
            return Optional.empty();
        }


        switch (data.getAnimationDataType()){
            case ATTACK -> {
                animation =  new AttackAnimation(player);
            }
            case COOL -> {
                animation =  new CoolAnimation(player);
            }
            default -> throw new IllegalStateException("Unexpected value: " + data.getAnimationDataType());
        }
        
        return Optional.of(animation);

    }
}
