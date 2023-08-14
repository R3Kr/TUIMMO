package protocol;

import game.actions.Action;

import java.util.Optional;

public class AnimationDataFactory {
    public static Optional<AnimationData> createAnimation(ActionData actionData){
        if (actionData.getDataType() == DataType.ATTACKDATA){
            return Optional.of(new AnimationData(AnimationData.AnimationDataType.ATTACK, actionData.getPlayerName()));
        } else if (actionData.getDataType() == DataType.COOLDATA) {
            return Optional.of(new AnimationData(AnimationData.AnimationDataType.COOL, actionData.getPlayerName()));
        }
        {
            return Optional.empty();
        }
    }
}
