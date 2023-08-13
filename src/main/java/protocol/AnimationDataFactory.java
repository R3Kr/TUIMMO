package protocol;

import game.actions.Action;

import java.util.Optional;

public class AnimationDataFactory {
    public static Optional<AnimationData> createAnimation(ActionData actionData){
        if (actionData.getDataType() == DataType.ATTACKDATA){
            return Optional.of(new AnimationData(actionData.getPlayerName()));
        }
        else {
            return Optional.empty();
        }
    }
}
