package client.states;

import java.io.IOException;

public interface ClientState {
    StateResult tick() throws IOException;

    void shutDown();

    enum StateResult{
        OK,
        EXIT
    }
}
