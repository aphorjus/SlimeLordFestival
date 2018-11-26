package game;

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

    public GameApi(GameClient gc) {
        gameClient = gc;
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
            onMessage(req);
        } else if (req.type.equals(GameApi.CreateEntity)) {
            onCreateEntity(req);
        } else if (req.type.equals(GameApi.DeleteEntity)) {
            onDeleteEntity(req);
        } else if (req.type.equals(GameApi.AlterGameState)) {
            onAlterGameState(req);
        } else if (req.type.equals(GameApi.AlterPlayerState)) {
            onAlterPlayerState(req);
        }
    }

    public void DeleteEntity(int id) {

    }

    public void CreateEntity() {}


    void onAlterGameState(GameApiRequest req) {
        if (req.type.equals(GameApi.SetGameStateOverworld)) {
            // Change game state
        } else if (req.type.equals(GameApi.SetGameStateBattle)) {
            // Change game state
        }
    }

    void onAlterPlayerState(GameApiRequest req) { }

    void onCreateEntity(GameApiRequest req) {
        // Create entity logic here
    }

    void onDeleteEntity(GameApiRequest req) {
        int entityId = req.body.getInt("entityId");
    }

    void onMessage(GameApiRequest req) {
        String msg = req.body.getString("text");

        System.out.println(msg);
    }
}
