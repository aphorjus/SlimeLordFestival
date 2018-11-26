package game.client.states;

import game.*;
import game.client.Board;
import game.client.GameClient;
import game.server.GameServer;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.lwjgl.opengl.EXTAbgr;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverworldState extends BasicGameState {
    InputManager inputManager;
    TextField textField;
    GameClient gameClient;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        gameClient = (GameClient)sbg;
        inputManager = gameClient.inputManager;
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        apiTest();
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        // Required to update before game process
        inputManager.update();
        Input input = gc.getInput();
        GameClient bg = (GameClient)sbg;
        Board board = bg.getBoard();
        if (input.isKeyDown(Input.KEY_LEFT)){
            board.moveLeft();
        }
        if (input.isKeyDown(Input.KEY_RIGHT)){
            board.moveRight();
        }
        if (input.isKeyDown(Input.KEY_UP)){
            board.moveUp();
        }
        if (input.isKeyDown(Input.KEY_DOWN)){
            board.moveDown();
        }
        board.update(delta);
        if (input.isKeyDown(Input.KEY_A)) {
            board.shiftLeft();
        }
        if (input.isKeyDown(Input.KEY_D)) {
            board.shiftRight();
        }
        if (input.isKeyDown(Input.KEY_W)) {
            board.shiftUp();
        }
        if (input.isKeyDown(Input.KEY_S)) {
            board.shiftDown();
        }
        if (input.isKeyDown(Input.KEY_B)) {
            bg.enterState(GameClient.BATTLE_STATE);
        }

        // Check the server for any incoming messages
        try {
            if (gameClient.input.available() > 0) {
                handleServerRequest(new GameApiRequest(new JSONObject(gameClient.input.readUTF())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        Board board = bg.getBoard();
        board.render(gc, sbg, g);
    }

    @Override
    public int getID() {
        return GameClient.OVERWORLD_STATE;
    }

    public void apiTest() {
        gameClient.sendMessage("this is a test message");
    }

    void handleServerRequest(GameApiRequest req) {
        System.out.println(req.toString());

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

    void onAlterGameState(GameApiRequest req) {
        SlimeGameState gameState = new SlimeGameState(req.body.getJSONObject("gameState"));
    }

    void onAlterPlayerState(GameApiRequest req) {
        PlayerState playerState = new PlayerState(req.body.getJSONObject("playerState"));
    }

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
