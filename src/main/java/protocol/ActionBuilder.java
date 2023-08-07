package protocol;

import game.Direction;
import game.Player;
import game.actions.Action;
import game.actions.Move;

import java.util.Map;

public class ActionBuilder {
    private Map<String, Player> players;

    private Player player;

    private Direction direction;

    public ActionBuilder(Map<String, Player> players) {
        this.players = players;
    }

    public ActionBuilder setPlayer(String playerName){
        this.player = players.get(playerName);
        return this;
    }

    public ActionBuilder setDirection(Direction direction){
        this.direction = direction;
        return this;
    }

    public Action build(){
        return new Move(player, direction);
    }


}
