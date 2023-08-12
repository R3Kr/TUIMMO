package client.states;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class LoginState implements ClientState{

    private Screen screen;

    private TextGraphics red;

    private TextGraphics green;

    private boolean test = true;

    public LoginState(Screen screen) throws IOException {
        this.screen = screen;
        screen.startScreen();
        this.red = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.green = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.GREEN);
    }

    @Override
    public StateResult tick() throws IOException {
        screen.refresh();
        screen.clear();

        KeyStroke keyStroke = screen.pollInput();


        if (keyStroke != null){
            test = !test;
            System.out.println("pressing");

            if (keyStroke.getKeyType() == KeyType.Escape){
                return StateResult.EXIT;
            }
        }
        render();
        return StateResult.OK;

    }

    @Override
    public void shutDown() {

    }


    private void render(){
        //System.out.println("rendering");
        if (test){
            System.out.println("red");
            red.putString(30,20, "tu madre es un caballo");
        }
        else {
            System.out.println("green");
            green.putString(30,20, "tu madre es un caballo");
        }


    }
}
