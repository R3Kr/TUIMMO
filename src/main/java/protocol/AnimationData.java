package protocol;

import java.io.*;


public class AnimationData implements Data {

    private String player;


    public AnimationData(String player) {
        this.player = player;
    }

    public AnimationData(byte[] bytes) throws IOException {
        write(bytes);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        dis.readShort(); // id already known
        player = dis.readUTF();
        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeShort(DataType.ANIMATIONDATA.getId());
        dos.writeUTF(player);

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    @Override
    public DataType getDataType() {
        return DataType.ANIMATIONDATA;
    }

    public String getPlayer() {
        return player;
    }
}
