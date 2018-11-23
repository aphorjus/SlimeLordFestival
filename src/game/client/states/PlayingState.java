package game.client.states;

import game.GameApi;
import game.GameApiRequest;
import game.InputManager;
import game.client.GameClient;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.lwjgl.opengl.EXTAbgr;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PlayingState extends BasicGameState {
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

        // Check the server for any incoming messages
        try {
            if (gameClient.input.available() > 0) {
                handleServerRequest(new JSONObject(gameClient.input.readUTF()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleServerRequest(JSONObject json) {
        System.out.println("received a thing");
        GameApiRequest req = new GameApiRequest(json);
        System.out.println(req.type);
        System.out.println(req.body.toString());
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) { }

    @Override
    public int getID() {
        return GameClient.PLAYING_STATE;
    }

    public void apiTest() {
        System.out.println("blah");
        gameClient.sendMessage("this is a test message");
    }
}
