package game.components;

import java.util.Objects;

public class NPC {
    private String name;
    private int x;
    private int y;

    public NPC() {
    }

    public NPC(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NPC npc = (NPC) o;
        return x == npc.x && y == npc.y && Objects.equals(name, npc.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, y);
    }

    public void setData(NPC npc) {
        x = npc.x;
        y = npc.y;

    }

    @Override
    public String toString() {
        return "NPC{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
