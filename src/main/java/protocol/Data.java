package protocol;

import java.io.IOException;

public interface Data {
    void write(byte[] bytes) throws IOException;
    byte[] read() throws IOException;
}
