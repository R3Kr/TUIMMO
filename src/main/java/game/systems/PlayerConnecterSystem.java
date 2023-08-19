package game.systems;

import com.esotericsoftware.minlog.Log;

import java.util.Queue;
import java.util.function.Consumer;

public class PlayerConnecterSystem implements System{

    private Queue<String> playersToConnect;

    private Queue<String> playersToDisconnect;
    private Consumer<String> spawnPlayer;
    private Consumer<String> despawnPlayer;

    private Runnable broadcastStateToClients;

    public PlayerConnecterSystem(Queue<String> playersToConnect, Queue<String> playersToDisconnect, Consumer<String> spawnPlayer, Consumer<String> despawnPlayer, Runnable broadcastStateToClients) {
        this.playersToConnect = playersToConnect;
        this.playersToDisconnect = playersToDisconnect;
        this.spawnPlayer = spawnPlayer;
        this.despawnPlayer = despawnPlayer;
        this.broadcastStateToClients = broadcastStateToClients;
    }

    @Override
    public void update() {
        while (!playersToConnect.isEmpty()){
            String player = playersToConnect.poll();
            Log.info(String.format("%s connected", player));
            spawnPlayer.accept(player);
            broadcastStateToClients.run();
        }

        while (!playersToDisconnect.isEmpty()){
            String player = playersToDisconnect.poll();
            Log.info(String.format("%s disconnected", player));
            despawnPlayer.accept(player);
            broadcastStateToClients.run();

        }
    }
}
