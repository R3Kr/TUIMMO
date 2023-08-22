package client;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.function.Predicate;

public class CooldownBar {
    private static final TerminalPosition POSITION = new TerminalPosition(50, 25);
    private static final TerminalPosition POSITION2 = new TerminalPosition(58, 25);
    private static final TerminalPosition POSITION3 = new TerminalPosition(65, 25);

    private final Predicate<Long> cantAttack;
    private final Predicate<Long> cantBlock;
    private final Predicate<Long> cantRegen;



    public CooldownBar(Predicate<Long> canAttack, Predicate<Long> canBlock, Predicate<Long> cantRegen) {
        this.cantAttack = canAttack;
        this.cantBlock = canBlock;
        this.cantRegen = cantRegen;
    }

    public void renderWith(TextGraphics white, TextGraphics gray){
        long timeNow = System.currentTimeMillis();
        if (cantAttack.test(timeNow)){

            gray.putString(POSITION, "ATTACK");
        }
        else {
            white.putString(POSITION, "ATTACK");

        }


        if (cantBlock.test(timeNow)){

            gray.putString(POSITION2, "BLOCK");
        }

        else {
            white.putString(POSITION2, "BLOCK");

        }

        if (cantRegen.test(timeNow)){

            gray.putString(POSITION3, "REGENERATION");
        }

        else {
            white.putString(POSITION3, "REGENERATION");

        }

    }
}
