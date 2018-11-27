package game.api;

import game.IGameState;
import game.client.Player;
import game.entities.IEntity;

public interface GameApiListener {
    void onAlterGameState(IGameState gameState);
    void onAlterPlayerState(Player player);
    void onCreateEntity(IEntity entity);
    void onDeleteEntity(int id);
    void onMessage(int senderId, String message);
    void onSetStateToBattle();
    void onSetStateToOverworld();
    void onEndTurn();

    void onLobbyClientListUpdate(String[] clientNames);
    void onLobbyIsFull();
}
