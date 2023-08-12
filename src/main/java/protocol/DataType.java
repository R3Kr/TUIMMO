package protocol;

/**
 * The DataType enum represents different types of data used in the protocol.
 */
public enum DataType {
    /**
     * Represents move data.
     */
    MOVEDATA((short) 0),

    /**
     * Represents attack data.
     */
    ATTACKDATA((short) 1);

    private final short id;

    /**
     * Constructs a DataType with the specified ID.
     *
     * @param id The ID associated with the data type.
     */
    DataType(short id) {
        this.id = id;
    }

    /**
     * Retrieves the ID associated with the data type.
     *
     * @return The ID of the data type.
     */
    public short getId() {
        return id;
    }

    /**
     * Converts a short value to the corresponding DataType enum value.
     *
     * @param s The short value to convert.
     * @return The corresponding DataType enum value, or null if not found.
     */
    public static DataType fromShort(short s) {
        for (DataType b : DataType.values()) {
            if (b.id == s) {
                return b;
            }
        }
        return null;
    }
}
