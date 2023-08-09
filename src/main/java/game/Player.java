package game;

import java.io.Serializable;

public class Player implements Movable {
    private final String name;
    private int x;
    private int y;

    private int maxHp;

    private int currHp;

    public Player(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.maxHp = 100;
        this.currHp = 100;
    }

    public void move(Direction direction){
        switch (direction){
            case DOWN -> {
                if (y < 23) y++;
            }
            case UP -> {
                if (y > 0) y--;
            }
            case LEFT -> {
                if (x > 0) x-=2;
            }
            case RIGHT -> {
                if (x < 79) x+=2;
            }
        }
    }

    public String getName() {
        return name;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrHp() {
        return currHp;
    }

    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isCloseTo(Player player){
        return x - player.x <= 2 && x - player.x >= -2 && y - player.y <= 1 && y - player.y >= -1;
    }
}
