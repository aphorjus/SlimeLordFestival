package game.Battles;

import jig.Vector;

public interface BattleEntity {

    public void onNextTurn();
    public void takeDamage(int amount);
    public boolean isAlive();
    public void setIndexes( int x, int y );
    public int getClientID();
    public int getMaxHP();
    public int getCurrentHP();
}
