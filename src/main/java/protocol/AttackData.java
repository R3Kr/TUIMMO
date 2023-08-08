package protocol;

import java.io.*;

public class AttackData implements ActionData {

    private static final short ATTACK = DataType.ATTACKDATA.getId();
    private String attackerName;
    private String targetName;

    public AttackData(byte[] bytes) throws IOException {
        write(bytes);
    }

    public AttackData(String playerName, String targetName) {
        this.attackerName = playerName;
        this.targetName = targetName;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        dis.readShort(); //id already known
        attackerName = dis.readUTF();
        targetName = dis.readUTF();

        dis.close();
    }

    @Override
    public byte[] read() throws IOException {
        byte[] bytes;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeShort(ATTACK);
        dos.writeUTF(attackerName);
        dos.writeUTF(targetName);

        bytes = bos.toByteArray();
        dos.close();
        return bytes;
    }


    public String getTargetName() {
        return targetName;
    }

    @Override
    public String getPlayerName() {
        return attackerName;
    }

    @Override
    public DataType getDataType() {
        return DataType.ATTACKDATA;
    }
}