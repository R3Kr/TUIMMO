package game.components;

import java.util.Objects;

public class NPC implements GameObject{
    private String name;
    private int x;
    private int y;

    private int currHp;

    private int zoneID;

    private String state = "neutral";

    public NPC() {
    }

    public NPC(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        currHp = getMaxHp();
        zoneID = 1;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getCurrHp() {
        return currHp;
    }

    @Override
    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    @Override
    public int getMaxHp() {
        return 100;
    }

    @Override
    public void setMaxHp(int maxHp) {

    }

    @Override
    public int getZoneID() {
        return zoneID;
    }

    @Override
    public void setZoneID(int zoneID) {
        this.zoneID = zoneID;
    }

    public String getState() {
        return state;
    }

    @Override
    public void takeDmg(int dmg) {
        GameObject.super.takeDmg(dmg);
        state = "attack";
    }

    @Override
    public boolean isInvincible() {
        return false;
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

    public void setData(GameObject gameObject) {
        NPC npc = (NPC) gameObject;
        x = npc.x;
        y = npc.y;
        currHp = npc.currHp;
        state = npc.state;
        zoneID = npc.zoneID;
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
