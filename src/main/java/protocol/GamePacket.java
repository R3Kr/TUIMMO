package protocol;

import java.io.IOException;
import java.net.DatagramPacket;

public interface GamePacket {
    DatagramPacket getPacket();
    void readData() throws IOException;
    void writeData() throws IOException;


}
