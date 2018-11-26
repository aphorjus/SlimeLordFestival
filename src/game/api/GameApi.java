package game.api;

import game.client.GameClient;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

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
    Socket serverSocket;
    DataInputStream input;
    DataOutputStream output;


    public GameApi(GameClient gc, GameApiListener listener) {
        this.gameClient = gc;
        this.listener = listener;
        this.input = gameClient.input;
        this.output = gameClient.output;
        this.serverSocket = gameClient.serverSocket;
    }

    public void update() {
        // Check the server for any incoming messages
        try {
            if (input.available() > 0) {
                handleServerRequest(new GameApiRequest(new JSONObject(input.readUTF())));
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

    public void sendMessage(String message) {
        if (message == null) return;

        JSONObject body = new JSONObject();
        body.put("message", message);
        sendRequest(new GameApiRequest(GameApi.Message, body));
    }

    public void setGameState(String newState) {
        if (!newState.equals(GameApi.SetGameStateBattle) || !newState.equals(GameApi.SetGameStateOverworld)) return;
        sendRequest(new GameApiRequest(newState));
    }

    void sendRequest(GameApiRequest req) {
        try {
            output.writeUTF(req.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
