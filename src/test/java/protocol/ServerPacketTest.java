package protocol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ServerPacketTest {

//    ServerPacket packet;
//    Collection<Player> players;

    @BeforeEach void setUp(){
//        packet = new ServerPacket(new DatagramPacket(new byte[1024], 1024));
//        players = new ArrayList<>();
//        players.add(new Player("as", 3, 5));
//        players.add(new Player("på", 3, 9));
//        players.add(new Player("sk", 12, 23));
    }
    @Test
    void readData() throws IOException {
//        packet.writeData(players);
//        packet.readData();
//        assertEquals(3, packet.getPlayerCount());
//        assertTrue(players.stream().map(Player::getName).toList().contains(packet.getPlayerNames()[0]));
    }

    @Test
    void writeData() throws IOException {
//        packet.writeData(players);
//        assertEquals(3, packet.getPlayerCount());
//        assertTrue(players.stream().map(Player::getName).toList().contains(packet.getPlayerNames()[0]));
    }
}