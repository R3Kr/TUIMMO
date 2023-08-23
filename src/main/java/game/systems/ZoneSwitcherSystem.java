package game.systems;

import com.googlecode.lanterna.TerminalPosition;
import game.actions.Direction;
import game.components.Player;
import protocol.data.ChangeZoneSignal;

import java.util.function.Consumer;

public class ZoneSwitcherSystem implements System {
    private static final TerminalPosition[] POSITIONS = new TerminalPosition[]{new TerminalPosition(38, 24), new TerminalPosition(38, 0), new TerminalPosition(0, 12), new TerminalPosition(78, 12)};
    private Player player;

    private Consumer<Object> sendToServer;

    public ZoneSwitcherSystem(Player player, Consumer<Object> sendToServer) {
        this.player = player;
        this.sendToServer = sendToServer;
    }

    @Override
    public void update() {
        if (player.getZoneID() == 0 && player.getX() == POSITIONS[1].getColumn() && player.getY() == POSITIONS[1].getRow()) {
            sendToServer.accept(new ChangeZoneSignal(1, Direction.UP));
        }
        if (player.getZoneID() == 1 && player.getX() == POSITIONS[0].getColumn() && player.getY() == POSITIONS[0].getRow()) {
            sendToServer.accept(new ChangeZoneSignal(0, Direction.DOWN));
        }

    }
}
