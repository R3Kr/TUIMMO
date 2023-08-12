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
    }

    public void run() throws IOException, InterruptedException {
        while (running){
            if (currentState.tick() == ClientState.StateResult.EXIT){
                changeState();
            }
            Thread.sleep(20);
        }
    }

    private void changeState() {
        if (currentState instanceof LoginState){
            currentState = playingState;
        }
        else {
            running = false;
        }
    }


    public void shutdown() {
        loginState.shutDown();
        playingState.shutDown();
    }
}
