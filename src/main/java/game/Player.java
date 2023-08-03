package game;

import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private int x;
    private int y;

    public Player(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void move(Direction direction){
        switch (direction){
            case DOWN -> y++;
            case UP -> y--;
            case LEFT -> x-=2;
            case RIGHT -> x+=2;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
