package game.client;

public interface GameApiListener {
    void onAlterGameState();
    void onAlterPlayerState();
    void onCreateEntity();
    void onDeleteEntity(int id);
    void onMessage(int senderId, String message);
}
