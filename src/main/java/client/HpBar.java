package client;

import com.googlecode.lanterna.TerminalPosition;

/**
 * The HpBar class represents a health bar display for a player using the Lanterna library.
 */
public class HpBar {
    public static final TerminalPosition POSITION = new TerminalPosition(2, 25);
    private static final int BAR_COUNT = 20;
    //private Player player;


    public HpBar() {
        //this.player = player;
    }

    /**
     * Generates and returns the formatted health bar string.
     *
     * @return The health bar string.
     */
    public String getString() {
        //return String.format("HP: [%s] (%d/%d)", healthString(), player.getCurrHp(), player.getMaxHp());
        return "";
    }

    private String healthString() {
        //int filledBars = player.getCurrHp() / (player.getMaxHp() / BAR_COUNT);
        //int emptyBars = BAR_COUNT - filledBars;

        return "";//"|".repeat(Math.max(filledBars, 0)) + " ".repeat(Math.min(emptyBars, BAR_COUNT));
    }
}
