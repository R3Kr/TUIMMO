package client.states;

import java.io.IOException;
import java.util.function.Supplier;

public interface ClientState {
    ClientState tick() throws IOException;

    void shutDown();

    void setOnNext(Supplier<ClientState> onNext);


    enum StateResult{
        OK,
        EXIT
    }
}
