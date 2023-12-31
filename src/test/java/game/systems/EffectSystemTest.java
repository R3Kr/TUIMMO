package game.systems;

import game.actions.Action;
import game.actions.Attack;
import game.components.Player;
import game.effects.BlockEffect;
import game.effects.Effect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class EffectSystemTest {
    Queue<Effect> effectQueue;
    EffectSystem effectSystem;
    @BeforeEach
    void init(){
        effectQueue = new LinkedList<>();
        effectSystem = new EffectSystem(effectQueue);
    }

    @Test
    void update() {
        Player player1 = new Player("test", 0, 0, 100);
        Player player2 = new Player("test2", 0, 0, 100);

        Action action = new Attack(player1, player2);
        action.perform();

        assertEquals(95, player2.getCurrHp());
        assertFalse(player2.isInvincible());

        Effect effect = new BlockEffect(player2);
        effectQueue.add(effect);
        effectSystem.update();
        assertTrue(effect.isActive());


        assertTrue(player2.isInvincible());


        action.perform();
        assertEquals(95, player2.getCurrHp());
    }


    @Test
    void randomTest(){
        ArrayList<Integer> ints = new ArrayList<>();
        Stream<Integer> ints2 = ints.stream();

        ints.add(12);

        assertTrue(ints2.toList().contains(12));





    }

    @Test
    void anotherTest(){

    }
}