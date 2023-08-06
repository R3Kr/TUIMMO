package protocol;

import java.io.*;
import java.net.DatagramPacket;

public class ClientPacket implements GamePacket{
    private DatagramPacket packet;
//    private ByteArrayInputStream bis;
//    private ByteArrayOutputStream bos;
//
//    private DataInputStream dis;
//    private DataOutputStream dos;

    private String playerName;
    private int x;
    private int y;


    public ClientPacket(DatagramPacket packet) {
        this.packet = packet;
//        if (packet.getLength() != 1024){
//            throw new IllegalArgumentException("packet is not 1024 bytes: (" + packet.getLength() + ")");
//        }

    }

    public DatagramPacket getPacket() {
        return packet;
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


    public void readData() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(packet.getData());
        DataInputStream dis = new DataInputStream(bis);

        playerName = dis.readUTF();
        x = dis.readInt();
        y = dis.readInt();

        dis.close();

    }

    public void writeData() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeUTF(playerName);
        dos.writeInt(x);
        dos.writeInt(y);

        packet.setData(bos.toByteArray());
        dos.close();

    }

    public void writeData(String playerName, int x, int y) throws IOException {
        this.playerName = playerName;
        this.x = x;
        this.y = y;

        writeData();

    }


}
