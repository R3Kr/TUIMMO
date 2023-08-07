package game;

public enum Direction {
    UP((short)0),
    DOWN((short)1),
    LEFT((short)2),
    RIGHT((short)3);

    private final short value;

    private Direction(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

    public static Direction fromShort(short s) {
        for (Direction b : Direction.values()) {
            if (b.value == s) { return b; }
        }
        return null;
    }
}
