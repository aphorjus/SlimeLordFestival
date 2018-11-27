package game.api;

import game.Battles.BattleGridTile;
import game.client.GameClient;
import game.client.Player;
import game.entities.IEntity;
import game.entities.slime.Slime;
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
    public static String EndTurn = "endTurn";


    public static String LobbyClientListUpdate = "lobbyClientListUpdate";
    public static String LobbyIsFull = "lobbyIsFull";
    public static String LobbyStartGame ="lobbyStartGame";

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
            listener.onCreateEntity(loadEntity(req.body.getString("entityType"), req.body));
        } else if (req.type.equals(GameApi.DeleteEntity)) {
            listener.onDeleteEntity(req.body.getInt("entityId"));
        } else if (req.type.equals(GameApi.AlterGameState)) {
        } else if (req.type.equals(GameApi.AlterPlayerState)) {
            listener.onAlterPlayerState(new Player(req.body));
        } else if (req.type.equals(GameApi.SetGameStateBattle)) {
            listener.onSetStateToBattle();
        } else if (req.type.equals(GameApi.SetGameStateOverworld)) {
            listener.onSetStateToOverworld();
        } else if (req.type.equals(GameApi.EndTurn)) {
            listener.onEndTurn();
        } else if (req.type.equals(GameApi.LobbyClientListUpdate)) {
            String[] clientNames = new String[req.body.getJSONArray("clientNames").length()];

            for (int i = 0; i < clientNames.length; i++) {
                clientNames[i] = req.body.getJSONArray("clientNames").getString(i);
            }

            listener.onLobbyClientListUpdate(clientNames);

            if (req.body.getInt("playerCount") == clientNames.length) {
                listener.onLobbyIsFull();
            }
        }
    }

    public void sendMessage(String message) {
        if (message == null) return;

        JSONObject body = new JSONObject();
        body.put("message", message);
        sendRequest(new GameApiRequest(GameApi.Message, body));
    }

    public void createEntity(IEntity entity) {
        sendRequest(new GameApiRequest(GameApi.CreateEntity, entity.toJson()));
    }

    public void deleteEntity(int entityId) {
        JSONObject body = new JSONObject();
        body.put("entityId", entityId);
        sendRequest(new GameApiRequest(GameApi.DeleteEntity, body));
    }

    public void updatePlayerState(Player player) {
        sendRequest(new GameApiRequest(GameApi.AlterPlayerState, player.toJson()));
    }

    public void setGameState(String newState) {
        if (!newState.equals(GameApi.SetGameStateBattle) && !newState.equals(GameApi.SetGameStateOverworld)) return;
        sendRequest(new GameApiRequest(newState));
    }

    void sendRequest(GameApiRequest req) {
        try {
            output.writeUTF(req.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    IEntity loadEntity(String entityType, JSONObject entityData) {
        switch (entityType) {
            case "BattleGridTile": return new BattleGridTile(entityData);

            case "Slime": return new Slime(entityData);

            default: return null;
        }
    }
}
