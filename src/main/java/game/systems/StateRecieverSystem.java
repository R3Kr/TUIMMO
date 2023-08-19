package game.systems;

import game.components.Player;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class StateRecieverSystem implements System{


    private Queue<Player> playersToUpdate;
    private List<Player> players;


    public StateRecieverSystem(Queue<Player> playersToUpdate, List<Player> players) {
        this.playersToUpdate = playersToUpdate;
        this.players = players;
    }

    @Override
    public void update() {
        while (!playersToUpdate.isEmpty()){
            Player player = playersToUpdate.poll();

            if (players.stream().noneMatch(p -> p.getName().equals(player.getName()))){
                players.add(player);
            }
            else {
                IntStream.range(0, players.size())
                        .filter(i -> players.get(i).getName().equals(player.getName()))
                        .findFirst()
                        .ifPresent(i -> players.get(i).setData(player));
            }
        }

    }
}
