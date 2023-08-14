package client.animations;

import game.Player;
import protocol.AnimationData;

import java.util.Map;
import java.util.Optional;

public class AnimationFactory {
    public static Optional<Animation> createAnimation(Map<String, Player> players, AnimationData data){
        Player player = players.get(data.getPlayer());
        if (player == null){
            return Optional.empty();
        }
        return Optional.of(new AttackAnimation(player));
    }
}
