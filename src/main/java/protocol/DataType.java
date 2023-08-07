package protocol;

public enum DataType {
    MOVEDATA((short) 0);

    private final short id;
    DataType(short id){
        this.id = id;
    }

    public short getId() {
        return id;
    }
}
