package game.components;

public interface GameObject {
     int getX();

     void setX(int x);

     int getY();

     void setY(int y);
     String getName();
     int getCurrHp();

     void setCurrHp(int currHp);

     int getMaxHp();

     void setMaxHp(int maxHp);

     void setData(GameObject gameObject);

     int getZoneID();

     void setZoneID(int zoneID);
    default boolean isCloseTo(GameObject gameObject) {
        return getZoneID() == gameObject.getZoneID() && getX() - gameObject.getX() >= -2 && getX() - gameObject.getX() <= 2 && getY() - gameObject.getY() >= -1 && getY() - gameObject.getY() <= 1;
    }

    default void takeDmg(int dmg){
        setCurrHp(getCurrHp()-dmg);
    }

    boolean isInvincible();
}
