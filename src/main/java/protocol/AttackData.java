package protocol;

import java.io.*;

/**
 * The AttackData class represents data related to an attack action.
 */
public class AttackData implements ActionData {
    private static final short ATTACK = DataType.ATTACKDATA.getId();
    private String attackerName;
    private String targetName;

    /**
     * Constructs an AttackData instance from a byte array.
     *
     * @param bytes The byte array containing the attack data.
     * @throws IOException If an I/O error occurs while processing the data.
     */
    public AttackData(byte[] bytes) throws IOException {
        write(bytes);
    }

    /**
     * Constructs an AttackData instance with the provided player names.
     *
     * @param playerName The name of the attacking player.
     * @param targetName The name of the target player.
     */
    public AttackData(String playerName, String targetName) {
        this.attackerName = playerName;
        this.targetName = targetName;
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        DataInputStream dis = new DataInputStream(bis);

        dis.readShort(); // id already known
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

    /**
     * Gets the name of the target player.
     *
     * @return The name of the target player.
     */
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
