package server;

import game.Player;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Clients {
    private Map<SocketAddress, String> clients;
    private final Map<String, Player> gameState;

    public Clients(GameState gameState) {
        this.gameState = gameState;
        this.clients = new ConcurrentHashMap<>();
    }

    public boolean connect(SocketAddress address, String player){

        return clients.putIfAbsent(address, player) == null && gameState.putIfAbsent(player, new Player(player, 0,0)) == null;
    }

    public boolean disconnect(SocketAddress address){
        String player = clients.remove(address);
        if (player == null){
            return false;
        }
        return gameState.remove(player) != null;
    }

    public boolean contains(SocketAddress address){
        return clients.keySet().stream().anyMatch(address1 -> address==address1);
    }

    public void forEach(Consumer<SocketAddress> consumer) {
        clients.keySet().forEach(consumer);
    }
}
