package protocol;

import java.io.*;

public class StayAliveData implements Data{
    private String player;

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

    public String getPlayer() {
        return player;
    }
}
