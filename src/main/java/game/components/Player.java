package game.components;



public class Player implements GameObject{
    private String name;
    private int x;
    private int y;

    private int currHp;
    private int maxHp;

    private boolean invincible = false;

    public Player(String name, int x, int y, int maxHp) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.currHp = maxHp;
        this.maxHp = maxHp;
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

    public void setData(Player player){

        x = player.x;
        y = player.y;
        currHp = player.currHp;
        maxHp = player.maxHp;

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

    public boolean isCloseTo(Player p) {
        return x - p.x >= -2 && x - p.x <= 2 && y - p.y >= -1 && y - p.y <= 1;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isInvincible() {
        return invincible;
    }
}
