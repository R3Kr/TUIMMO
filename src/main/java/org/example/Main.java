package org.example;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import javax.swing.*;
import java.io.IOException;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg= screen.newTextGraphics();


        screen.startScreen(); // screens must be started
        screen.clear();
        int x = 5;
        int y = 5;
        boolean run = true;


        tg.setForegroundColor(TextColor.ANSI.WHITE);
        tg.setBackgroundColor(TextColor.ANSI.BLUE);



        while (run){

            screen.refresh();
            screen.clear();
            // Read input (non-blocking)
            KeyStroke keyStroke = screen.pollInput();

            // Handle input
            if (keyStroke != null) {
                switch (keyStroke.getKeyType()) {
                    case ArrowUp -> y--;
                    case ArrowDown -> y++;
                    case ArrowLeft -> x--;
                    case ArrowRight -> x++;
                    case Escape -> {
                        run = false;
                    }
                    default -> {
                    }
                }
            }

            tg.putString(x, y, "P");
            tg.drawLine(0, 0, 30, 40, 'E');

            System.out.println(tg.getSize());
            Thread.sleep(20);
        }

        screen.stopScreen(); // screens must be stopped when done
    }
}