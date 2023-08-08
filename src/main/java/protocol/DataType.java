package protocol;

import game.Direction;

public enum DataType {
    MOVEDATA((short) 0),
    ATTACKDATA((short)1);

    private final short id;
    DataType(short id){
        this.id = id;
    }
    public static DataType fromShort(short s) {
        for (DataType b : DataType.values()) {
            if (b.id == s) { return b; }
        }
        return null;
    }

    public short getId() {
        return id;
    }
}
