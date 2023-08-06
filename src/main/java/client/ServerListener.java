package client;

import game.Player;
import protocol.ServerPacket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class ServerListener implements Runnable{

    private DatagramSocket socket;

    private ServerPacket sp;
    private ConcurrentHashMap<String, Player> players;

    public ServerListener(ConcurrentHashMap<String, Player> players, DatagramSocket socket) {
        this.socket = socket;
        this.players = players;
        this.sp = new ServerPacket(new DatagramPacket(new byte[1024], 1024));
    }

    @Override
    public void run() {
        while (true){
            try {
                //System.out.println("Waiting to recieve");
                socket.receive(sp.getPacket());
                //System.out.println("Just recieved");
                sp.readData();
            } catch (IOException e) {
                System.err.println(e);
                continue;
            }
            printPlayers();

            if (players.size() < sp.getPlayerCount()){
                addPlayer();
            }

            if (players.size() > sp.getPlayerCount()){
                removePlayer();
            }

            for (int i = 0; i < sp.getPlayerCount(); i++) {
                Player player = players.get(sp.getPlayerNames()[i]);
                player.setX(sp.getX()[i]);
                player.setY(sp.getY()[i]);
            }


        }
    }

    private void removePlayer() {
        Iterator<String> it = players.keys().asIterator();

        for (String player = it.next(); it.hasNext(); player = it.next()) {
            if (!Arrays.asList(sp.getPlayerNames()).contains(player)){
                players.remove(player);
            }
        }
    }

    private void addPlayer(){
        for (int i = 0; i < sp.getPlayerCount(); i++) {
            players.putIfAbsent(sp.getPlayerNames()[i], new Player(sp.getPlayerNames()[i], sp.getX()[i], sp.getY()[i]));
        }

    }

    private void printPlayers(){
        System.out.println(sp.getPlayerCount());
        for (int i = 0; i < sp.getPlayerCount(); i++) {
            System.out.printf("%s %d %d", sp.getPlayerNames()[i], sp.getX()[i], sp.getY()[i]);
        }
    }
}
