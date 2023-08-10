package client;

import client.states.ClientState;

import java.io.IOException;

public class ClientContext {
    private ClientState currentState;


    public ClientContext(ClientState currentState) {
        this.currentState = currentState;
    }

    public void run() throws IOException, InterruptedException {
        while (true){
            currentState.tick();
            Thread.sleep(20);
        }
    }
}
