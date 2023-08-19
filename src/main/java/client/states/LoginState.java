package client.states;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import protocol.data.ConnectPlayer;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LoginState implements ClientState{

    private Screen screen;

    private TextGraphics red;

    private TextGraphics green;

    private boolean test = true;
    private Supplier<ClientState> onNext;
    private Consumer<ConnectPlayer> onLogin;

    private String playerName;

    public LoginState(Screen screen, Consumer<ConnectPlayer> onLogin, String name) throws IOException {
        this.screen = screen;
        this.onLogin = onLogin;
        this.playerName = name;
        screen.startScreen();
        this.red = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.RED);
        this.green = screen.newTextGraphics().setForegroundColor(TextColor.ANSI.GREEN);
    }

    @Override
    public ClientState tick() throws IOException {
        screen.refresh();
        screen.clear();

        KeyStroke keyStroke = screen.pollInput();


        if (keyStroke != null){
            test = !test;


            if (keyStroke.getKeyType() == KeyType.Escape){
                onLogin.accept(new ConnectPlayer(playerName));
                return onNext.get();
            }
        }
        render();
        return this;

    }

    @Override
    public void shutDown() {

    }

    @Override
    public void setOnNext(Supplier<ClientState> onNext) {
        this.onNext = onNext;
    }

    public String getPlayerName() {
        return playerName;
    }

    private void render(){
        //System.out.println("rendering");
        if (test){

            red.putString(30,20, "tu madre es un caballo");
        }
        else {

            green.putString(30,20, "tu madre es un caballo");
        }


    }
}
