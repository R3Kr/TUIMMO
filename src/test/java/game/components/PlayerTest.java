package game.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void isCloseTo() {
        Player player = new Player("asd", 30, 30, 100);
        Player player2 = new Player("asd", 31, 31, 100);
        Player player3 = new Player("asd", 40, 40, 100);

        assertTrue(player.isCloseTo(player2));
        assertFalse(player.isCloseTo(player3));
    }
}