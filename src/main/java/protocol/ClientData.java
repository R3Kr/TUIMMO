package protocol;

import game.Direction;

import java.io.*;

public class ClientData implements Data {

    private static final short MOVE = DataType.MOVEDATA.getId();
    private String playerName;
    private Direction direction;

    public ClientData(byte[] bytes) throws IOException {
        write(bytes);
    }

    public ClientData(String playerName, Direction direction) {
        this.playerName = playerName;
        this.direction = direction;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        dis.readShort(); //id already known
        playerName = dis.readUTF();
        direction = Direction.fromShort(dis.readShort());

        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeShort(MOVE);
        dos.writeUTF(playerName);
        dos.writeShort(direction.getValue());

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Direction getDirection() {
        return direction;
    }
}