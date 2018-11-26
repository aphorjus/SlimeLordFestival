package game.api;

import game.IGameState;
import game.client.IPlayerState;
import game.entities.IEntity;

public interface GameApiListener {
    void onAlterGameState(IGameState gameState);
    void onAlterPlayerState(IPlayerState playerState);
    void onCreateEntity(IEntity entity);
    void onDeleteEntity(int id);
    void onMessage(int senderId, String message);
    void onSetStateToBattle();
    void onSetStateToOverworld();
}
