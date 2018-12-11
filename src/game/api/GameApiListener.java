package game.api;

import game.IGameState;
import game.client.Player;
import game.entities.IEntity;
import game.entities.slimelord.SlimeLord;

public interface GameApiListener {
    void onAlterGameState(IGameState gameState);
    void onAlterPlayerState(Player player);
    void onCreateEntity(IEntity entity);
    void onDeleteEntity(int id);
    void onMessage(int senderId, String message);
    void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo);
    void onSetStateToOverworld();
    void onEndTurn();
    void onConnectionConfirmation(int myId);

    void onLobbyClientListUpdate(String[] clientNames);
    void onLobbyIsFull();
}
