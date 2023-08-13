package client.animations;

import game.Player;
import protocol.AnimationData;

import java.util.Map;
import java.util.Optional;

public class AnimationBuilderFactory {
    public static Optional<AnimationBuilder> createAnimation(Map<String, Player> players, AnimationData data){
        Player player = players.get(data.getPlayer());
        if (player == null){
            return Optional.empty();
        }
        return Optional.of(new AnimationBuilder().set(player));
    }
}
