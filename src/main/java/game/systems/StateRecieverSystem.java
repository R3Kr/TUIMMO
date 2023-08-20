package game.systems;

import game.components.NPC;
import game.components.Player;

import java.util.List;
import java.util.Queue;
import java.util.stream.IntStream;

public class StateRecieverSystem implements System{


    private Queue<Player> playersToUpdate;
    private Queue<NPC> npcsToUpdate;
    private List<Player> players;
    private List<NPC> npcs;


    public StateRecieverSystem(Queue<Player> playersToUpdate, Queue<NPC> npcsToUpdate, List<Player> players, List<NPC> npcs) {
        this.playersToUpdate = playersToUpdate;
        this.npcsToUpdate = npcsToUpdate;
        this.players = players;
        this.npcs = npcs;
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

        while (!npcsToUpdate.isEmpty()){
            NPC npc = npcsToUpdate.poll();

            if (npcs.stream().noneMatch(p -> p.getName().equals(npc.getName()))){
                npcs.add(npc);
            }
            else {
                IntStream.range(0, npcs.size())
                        .filter(i -> npcs.get(i).getName().equals(npc.getName()))
                        .findFirst()
                        .ifPresent(i -> npcs.get(i).setData(npc));
            }
        }

    }
}
