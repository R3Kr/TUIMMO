package protocol;

import game.Player;

import java.io.*;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ServerPacket implements GamePacket{
    private DatagramPacket packet;

    private int playerCount;

    private String[] playerNames;
    private int[] x;
    private int[] y;

    public ServerPacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public DatagramPacket getPacket() {
        return packet;
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

    public void readData() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
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

    public void writeData() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeInt(playerCount);

        for (int i = 0; i < playerCount; i++){
            dos.writeUTF(playerNames[i]);
            dos.writeInt(x[i]);
            dos.writeInt(y[i]);
        }

        packet.setData(bos.toByteArray());
        dos.close();

    }

    public void writeData(Collection<Player> players) throws IOException {
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

        writeData();
    }
}
