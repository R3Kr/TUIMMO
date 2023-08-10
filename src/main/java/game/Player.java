package game;

/**
 * The Player class represents a player in the game.
 */
public class Player implements Movable {
    private final String name;
    private int x;
    private int y;

    private int maxHp;

    private int currHp;

    /**
     * Constructs a new Player instance with the given name and initial coordinates.
     *
     * @param name The name of the player.
     * @param x    The initial x-coordinate.
     * @param y    The initial y-coordinate.
     */
    public Player(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.maxHp = 100;
        this.currHp = 100;
    }

    /**
     * Moves the player in the specified direction.
     *
     * @param direction The direction in which to move the player.
     */
    public void move(Direction direction) {
        switch (direction) {
            case DOWN -> {
                if (y < 23) y++;
            }
            case UP -> {
                if (y > 0) y--;
            }
            case LEFT -> {
                if (x > 0) x -= 2;
            }
            case RIGHT -> {
                if (x < 79) x += 2;
            }
        }
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the x-coordinate of the player.
     *
     * @return The x-coordinate of the player.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the player.
     *
     * @return The y-coordinate of the player.
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the maximum health points of the player.
     *
     * @return The maximum health points of the player.
     */
    public int getMaxHp() {
        return maxHp;
    }

    /**
     * Gets the current health points of the player.
     *
     * @return The current health points of the player.
     */
    public int getCurrHp() {
        return currHp;
    }

    /**
     * Sets the current health points of the player.
     *
     * @param currHp The new current health points of the player.
     */
    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    /**
     * Sets the x-coordinate of the player.
     *
     * @param x The new x-coordinate of the player.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of the player.
     *
     * @param y The new y-coordinate of the player.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Checks if the player is close to another player.
     *
     * @param player The other player to check proximity against.
     * @return True if the players are close, false otherwise.
     */
    public boolean isCloseTo(Player player) {
        return x - player.x <= 2 && x - player.x >= -2 && y - player.y <= 1 && y - player.y >= -1;
    }
}
