package protocol;

import java.io.*;
import java.net.DatagramPacket;

public class StayAlivePacket implements GamePacket{
    public static final int SEND_RATE = 1000;
    private DatagramPacket packet;
    private String player;

    public StayAlivePacket(DatagramPacket packet) {
        this.packet = packet;
    }

    public String getPlayer() {
        return player;
    }

    @Override
    public DatagramPacket getPacket() {
        return packet;
    }

    @Override
    public void readData() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
        DataInputStream dis = new DataInputStream(bis);

        player = dis.readUTF();
        dis.close();
    }

    @Override
    public void writeData() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeUTF(player);

        packet.setData(bos.toByteArray());
        dos.close();
    }

    public void writeData(String player) throws IOException {
        this.player = player;

        writeData();
    }
}
