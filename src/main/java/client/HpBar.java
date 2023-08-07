package client;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;
import game.Player;

public class HpBar {
    public static final TerminalPosition POSITION = new TerminalPosition(2, 25);
    private static final int BAR_COUNT = 20;
    private Player player;

    public HpBar(Player player) {
        this.player = player;
    }

    public String getString(){
        return String.format("HP: [%s] (%d/%d)", healthString(), player.getCurrHp(), player.getMaxHp());
    }

    private String healthString(){


        int filledBars = player.getCurrHp()/(player.getMaxHp()/BAR_COUNT);

        int emptyBars = BAR_COUNT - filledBars;


        return "|".repeat(filledBars) + " ".repeat(emptyBars);

    }




}
