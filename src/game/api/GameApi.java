package game.api;

import game.client.GameClient;
import org.json.JSONObject;

public class GameApi {
    public static String AlterGameState = "alterGameState";
    public static String AlterPlayerState = "alterPlayerState";
    public static String ConnectionConfirmation = "connectionConfirmation";
    public static String CreateEntity = "createEntity";
    public static String DeleteEntity = "deleteEntity";
    public static String Message = "message";
    public static String SetGameStateOverworld = "setOverworld";
    public static String SetGameStateBattle = "setBattle";

    GameClient gameClient;
    GameApiListener listener;

    public GameApi(GameClient gc, GameApiListener listener) {
        this.gameClient = gc;
        this.listener = listener;
    }

    public void update() {
        // Check the server for any incoming messages
        try {
            if (gameClient.input.available() > 0) {
                handleServerRequest(new GameApiRequest(new JSONObject(gameClient.input.readUTF())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleServerRequest(GameApiRequest req) {
        if (req.type.equals(GameApi.Message)) {
            listener.onMessage(req.body.getInt("senderId"), req.body.getString("message"));
        } else if (req.type.equals(GameApi.CreateEntity)) {
        } else if (req.type.equals(GameApi.DeleteEntity)) {
            listener.onDeleteEntity(req.body.getInt("entityId"));
        } else if (req.type.equals(GameApi.AlterGameState)) {
        } else if (req.type.equals(GameApi.AlterPlayerState)) {
        }
    }
}
