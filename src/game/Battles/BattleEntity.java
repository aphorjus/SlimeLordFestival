package game.Battles;

public interface BattleEntity {

    public void onNextTurn();
    public void takeDamage(int amount);
    public boolean isAlive();
}
