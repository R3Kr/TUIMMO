package client.states;

import java.io.IOException;

public interface ClientState {
    void tick() throws IOException;
}
