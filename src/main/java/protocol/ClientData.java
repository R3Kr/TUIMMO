package protocol;

import java.io.*;

public class ClientData implements Data {

    private String playerName;
    private int x;
    private int y;

    public ClientData(byte[] bytes) throws IOException {
        write(bytes);
    }

    public ClientData(String playerName, int x, int y) {
        this.playerName = playerName;
        this.x = x;
        this.y = y;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        playerName = dis.readUTF();
        x = dis.readInt();
        y = dis.readInt();

        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeUTF(playerName);
        dos.writeInt(x);
        dos.writeInt(y);

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}