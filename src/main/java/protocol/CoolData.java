package protocol;

import java.io.*;

public class CoolData implements ActionData{
    private String player;

    /**
     * Constructs a StayAliveData object for the specified player.
     *
     * @param player The name of the player to keep alive.
     */
    public CoolData(String player) {
        this.player = player;
    }

    public CoolData(byte[] bytes) throws IOException {
        write(bytes);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        dis.readShort(); //id already known
        player = dis.readUTF();
        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeShort(DataType.COOLDATA.getId());
        dos.writeUTF(player);

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    @Override
    public String getPlayerName() {
        return player;
    }

    @Override
    public DataType getDataType() {
        return DataType.COOLDATA;
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
