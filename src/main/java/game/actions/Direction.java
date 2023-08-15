package game.actions;

/**
 * The Direction enum represents different directions for movement in the game.
 */
public enum Direction {
    UP((short) 0),
    DOWN((short) 1),
    LEFT((short) 2),
    RIGHT((short) 3);

    private final short value;

    /**
     * Constructs a new Direction enum value with the given value.
     *
     * @param value The numerical value associated with the direction.
     */
    private Direction(short value) {
        this.value = value;
    }

    /**
     * Gets the numerical value associated with the direction.
     *
     * @return The numerical value of the direction.
     */
    public short getValue() {
        return value;
    }

    /**
     * Converts a short value to the corresponding Direction enum value.
     *
     * @param s The short value to convert.
     * @return The Direction enum value corresponding to the short value, or null if not found.
     */
    public static Direction fromShort(short s) {
        for (Direction b : Direction.values()) {
            if (b.value == s) {
                return b;
            }
        }
        return null;
    }
}
