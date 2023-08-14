package protocol;

import java.io.*;


public class AnimationData implements Data {

    private AnimationDataType dataType;
    private String player;


    public AnimationData(AnimationDataType dataType, String player) {
        this.dataType = dataType;
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
        dataType = AnimationDataType.fromShort(dis.readShort());
        player = dis.readUTF();
        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeShort(DataType.ANIMATIONDATA.getId());
        dos.writeShort(dataType.id);
        dos.writeUTF(player);

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }

    @Override
    public DataType getDataType() {
        return DataType.ANIMATIONDATA;
    }

    public AnimationDataType getAnimationDataType(){
        return dataType;
    }

    public String getPlayer() {
        return player;
    }

    public enum AnimationDataType{

        ATTACK((short) 0),
        COOL((short) 1);

        private final short id;

        AnimationDataType(short id) {
            this.id = id;
        }
        public short getId() {
            return id;
        }

        public static AnimationDataType fromShort(short s) {
            for (AnimationDataType b : AnimationDataType.values()) {
                if (b.id == s) {
                    return b;
                }
            }
            return null;
        }
    }
}
