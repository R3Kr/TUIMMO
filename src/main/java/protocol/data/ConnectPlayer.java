package protocol.data;

public class ConnectPlayer {
    public String player;
    public int connectionId;

    public ConnectPlayer() {
    }

    public ConnectPlayer(String player) {
        this.player = player;
    }
}
