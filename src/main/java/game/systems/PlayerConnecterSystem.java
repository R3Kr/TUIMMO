package game.systems;

import com.esotericsoftware.minlog.Log;
import protocol.data.ConnectPlayer;

import java.util.Queue;
import java.util.function.Consumer;

public class PlayerConnecterSystem implements System{

    private Queue<ConnectPlayer> playersToConnect;

    private Queue<String> playersToDisconnect;
    private Consumer<String> spawnPlayer;
    private Consumer<String> despawnPlayer;

    private Consumer<ConnectPlayer> broadcastLoginToClients;

    public PlayerConnecterSystem(Queue<ConnectPlayer> playersToConnect, Queue<String> playersToDisconnect, Consumer<String> spawnPlayer, Consumer<String> despawnPlayer, Consumer<ConnectPlayer> broadcastStateToClients) {
        this.playersToConnect = playersToConnect;
        this.playersToDisconnect = playersToDisconnect;
        this.spawnPlayer = spawnPlayer;
        this.despawnPlayer = despawnPlayer;
        this.broadcastLoginToClients = broadcastStateToClients;
    }

    @Override
    public void update() {
        while (!playersToConnect.isEmpty()){
            ConnectPlayer connectPlayer = playersToConnect.poll();
            Log.info(String.format("%s connected", connectPlayer.player));
            spawnPlayer.accept(connectPlayer.player);
            broadcastLoginToClients.accept(connectPlayer);
        }

        while (!playersToDisconnect.isEmpty()){
            String player = playersToDisconnect.poll();
            Log.info(String.format("%s disconnected", player));
            despawnPlayer.accept(player);

        }
    }
}
