package game.client.states;

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
        textField = new TextField(gc, gc.getDefaultFont(), 100, 50, 200, 100);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        // Required to update before game process
        inputManager.update();

        try {
            if (gameClient.input.available() > 0) {
                handleServerMessage(new JSONObject(gameClient.input.readUTF()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (textField.hasFocus()) {
            textFieldProcess(delta);
            return;
        }
    }

    void handleServerMessage(JSONObject msg) {
        System.out.println(msg.toString());
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {
        textField.render(gc, g);
    }

    @Override
    public int getID() {
        return GameClient.PLAYING_STATE;
    }

    void textFieldProcess(int delta) {
        if (inputManager.justPressed(Input.KEY_ENTER)) {
            gameClient.sendMessage(textField.getText());
            textField.setText("");
        }
    }
}
