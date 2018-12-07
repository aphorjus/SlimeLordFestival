package game.Battles;

public interface BattleEntity {

    public void onNextTurn();
    public void takeDamage(int amount);
    public boolean isAlive();
    public void setIndexes( int x, int y );
}
