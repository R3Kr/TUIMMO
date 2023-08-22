package client;

import client.states.ClientState;
import client.states.LoginState;
import client.states.PlayingState;

import java.io.IOException;

public class ClientContext {
    private ClientState currentState;

    private ClientState loginState;

    private ClientState playingState;
    private boolean running = true;


    public ClientContext(ClientState loginState, ClientState playingState) {
        this.loginState = loginState;
        this.playingState = playingState;
        this.currentState = loginState;
        init();
    }

    private void init(){
        loginState.setOnNext(() -> playingState);
        playingState.setOnNext(() -> loginState);
    }

    public void run() throws IOException, InterruptedException {
        while (running){
            currentState = currentState.tick();
            if (currentState == null){
                running = false;
            }
            Thread.sleep(20);
        }
    }



    public void shutdown() {
        loginState.shutDown();
        playingState.shutDown();
    }
}
