package game.components;



public class Player implements GameObject{
    private String name;
    private int x;
    private int y;

    private int currHp;
    private int maxHp;

    private int zoneId;

    private boolean invincible = false;

    public Player(String name, int x, int y, int maxHp) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.currHp = maxHp;
        this.maxHp = maxHp;
        this.zoneId = 0;
    }

    public Player() {
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public void setData(GameObject gameObject){
        Player player = (Player) gameObject;
        x = player.x;
        y = player.y;
        currHp = player.currHp;
        maxHp = player.maxHp;
        zoneId = player.zoneId;

    }

    @Override
    public int getZoneID() {
        return zoneId;
    }

    @Override
    public void setZoneID(int zoneID) {
        this.zoneId = zoneID;
    }

    public void print(){
        System.out.printf("%d %d", x, y);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", currHp=" + currHp +
                ", maxHp=" + maxHp +
                '}';
    }


    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
