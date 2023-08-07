package protocol;

import game.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

public class ServerData implements Data{
    private int playerCount;

    private String[] playerNames;
    private int[] x;
    private int[] y;

    public ServerData(Collection<Player> players) {
        playerCount = players.size();
        playerNames = new String[playerCount];
        x = new int[playerCount];
        y = new int[playerCount];

        ArrayList<Player> playerList = new ArrayList<>(players);

        for (int i = 0; i < playerCount; i++){
            playerNames[i] = playerList.get(i).getName();
            x[i] = playerList.get(i).getX();
            y[i] = playerList.get(i).getY();
        }
    }

    public ServerData(byte[] bytes) throws IOException {
        write(bytes);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        playerCount = dis.readInt();
        playerNames = new String[playerCount];
        x = new int[playerCount];
        y = new int[playerCount];

        for (int i = 0; i < playerCount; i++){
            playerNames[i] = dis.readUTF();
            x[i] = dis.readInt();
            y[i] = dis.readInt();
        }


        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeInt(playerCount);

        for (int i = 0; i < playerCount; i++){
            dos.writeUTF(playerNames[i]);
            dos.writeInt(x[i]);
            dos.writeInt(y[i]);
        }

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }
}
