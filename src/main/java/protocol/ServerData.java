package protocol;

import game.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The ServerData class represents data related to the current state of players on the server.
 */
public class ServerData implements Data {

    private int playerCount;
    private String[] playerNames;
    private int[] x;
    private int[] y;
    private int[] currHps;

    /**
     * Constructs a ServerData object based on the collection of players.
     *
     * @param players The collection of players to represent the server state.
     */
    public ServerData(Collection<Player> players) {
        playerCount = players.size();
        playerNames = new String[playerCount];
        x = new int[playerCount];
        y = new int[playerCount];
        currHps = new int[playerCount];

        ArrayList<Player> playerList = new ArrayList<>(players);

        for (int i = 0; i < playerCount; i++) {
            playerNames[i] = playerList.get(i).getName();
            x[i] = playerList.get(i).getX();
            y[i] = playerList.get(i).getY();
            currHps[i] = playerList.get(i).getCurrHp();
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
        currHps = new int[playerCount];

        for (int i = 0; i < playerCount; i++) {
            playerNames[i] = dis.readUTF();
            x[i] = dis.readInt();
            y[i] = dis.readInt();
            currHps[i] = dis.readInt();
        }

        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeInt(playerCount);

        for (int i = 0; i < playerCount; i++) {
            dos.writeUTF(playerNames[i]);
            dos.writeInt(x[i]);
            dos.writeInt(y[i]);
            dos.writeInt(currHps[i]);
        }

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    /**
     * Retrieves the number of players in the server state.
     *
     * @return The player count.
     */
    public int getPlayerCount() {
        return playerCount;
    }

    /**
     * Retrieves an array containing the names of the players.
     *
     * @return An array of player names.
     */
    public String[] getPlayerNames() {
        return playerNames;
    }

    /**
     * Retrieves an array containing the X coordinates of the players.
     *
     * @return An array of X coordinates.
     */
    public int[] getX() {
        return x;
    }

    /**
     * Retrieves an array containing the Y coordinates of the players.
     *
     * @return An array of Y coordinates.
     */
    public int[] getY() {
        return y;
    }

    /**
     * Retrieves an array containing the current hit points of the players.
     *
     * @return An array of current hit points.
     */
    public int[] getCurrHps() {
        return currHps;
    }
}
