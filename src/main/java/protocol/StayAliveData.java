package protocol;

import java.io.*;

/**
 * The StayAliveData class represents data to keep a player connected on the server.
 */
public class StayAliveData implements Data {

    private String player;

    /**
     * Constructs a StayAliveData object for the specified player.
     *
     * @param player The name of the player to keep alive.
     */
    public StayAliveData(String player) {
        this.player = player;
    }

    public StayAliveData(byte[] bytes) throws IOException {
        write(bytes);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        player = dis.readUTF();
        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeUTF(player);

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    /**
     * Retrieves the name of the player for whom the stay-alive data is intended.
     *
     * @return The player's name.
     */
    public String getPlayer() {
        return player;
    }
}
