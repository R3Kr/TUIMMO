package client;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.function.Predicate;

public class CooldownBar {
    private static final TerminalPosition POSITION = new TerminalPosition(60, 25);
    private static final TerminalPosition POSITION2 = new TerminalPosition(70, 25);

    private final Predicate<Long> cantAttack;
    private final Predicate<Long> cantBlock;



    public CooldownBar(Predicate<Long> canAttack, Predicate<Long> canBlock) {
        this.cantAttack = canAttack;
        this.cantBlock = canBlock;
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

    }
}
