package client;

import game.Player;
import protocol.ServerData;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ServerListener implements Runnable{

    private DatagramSocket socket;

    private DatagramPacket packet;
    private ConcurrentHashMap<String, Player> players;

    public ServerListener(ConcurrentHashMap<String, Player> players, DatagramSocket socket) {
        this.socket = socket;
        this.players = players;
        this.packet = new DatagramPacket(new byte[1024], 1024);
    }

    @Override
    public void run() {
        while (true){
            ServerData data;
            try {
                //System.out.println("Waiting to recieve");
                socket.receive(packet);
                //System.out.println("Just recieved");
                data = new ServerData(packet.getData());
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            printServerPacket(data);

            if (players.size() < data.getPlayerCount()){
                addPlayer(data);
            }

            if (players.size() > data.getPlayerCount()){
                removePlayer(data);
            }

            for (int i = 0; i < data.getPlayerCount(); i++) {
                Player player = players.get(data.getPlayerNames()[i]);
                player.setX(data.getX()[i]);
                player.setY(data.getY()[i]);
                player.setCurrHp(data.getCurrHps()[i]);
            }

            System.out.println(players);


        }
    }

    private void removePlayer(ServerData data) {
        System.out.println("removePlayer");
        Iterator<String> it = players.keys().asIterator();


        while (it.hasNext()) {
            String player = it.next();
            System.out.println(player);
            if (!Arrays.asList(data.getPlayerNames()).contains(player)){
                players.remove(player);
            }
        }
    }

    private void addPlayer(ServerData data){
        for (int i = 0; i < data.getPlayerCount(); i++) {
            players.putIfAbsent(data.getPlayerNames()[i], new Player(data.getPlayerNames()[i], data.getX()[i], data.getY()[i]));
        }

    }

    private void printServerPacket(ServerData data){
        System.out.println(data.getPlayerCount());
        for (int i = 0; i < data.getPlayerCount(); i++) {
            System.out.printf("%s %d %d %d", data.getPlayerNames()[i], data.getX()[i], data.getY()[i], data.getCurrHps()[i]);
        }
    }
}
