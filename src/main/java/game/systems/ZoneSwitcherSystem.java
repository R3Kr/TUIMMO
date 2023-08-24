package game.systems;

import com.googlecode.lanterna.TerminalPosition;
import game.actions.Action;
import game.actions.ChangeZone;
import game.actions.Direction;
import game.components.GameObject;
import game.components.Player;
import protocol.data.ChangeZoneSignal;

import java.util.Queue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ZoneSwitcherSystem implements System {
    private static final TerminalPosition[] POSITIONS = new TerminalPosition[]{new TerminalPosition(38, 24), new TerminalPosition(38, 0), new TerminalPosition(0, 12), new TerminalPosition(78, 12)};
    private Supplier<Stream<Player>> players;
    private Queue<Action> actionQueue;
    public ZoneSwitcherSystem(Supplier<Stream<Player>> players, Queue<Action> actionQueue) {
        this.players = players;
        this.actionQueue = actionQueue;
    }

    @Override
    public void update() {
        players.get().forEach(player ->{
            if (player.getZoneID() == 0 && player.getX() == POSITIONS[1].getColumn() && player.getY() == POSITIONS[1].getRow()) {
                actionQueue.offer(new ChangeZone(player, 1, Direction.UP));
            }
            if (player.getZoneID() == 1 && player.getX() == POSITIONS[0].getColumn() && player.getY() == POSITIONS[0].getRow()) {
                actionQueue.offer(new ChangeZone(player, 0, Direction.DOWN));
            }
            if (player.getCurrHp() <= 0) {
                player.setCurrHp(player.getMaxHp());
                actionQueue.offer(new ChangeZone(player, 0, Direction.UP));
            }

        });

    }
    }
