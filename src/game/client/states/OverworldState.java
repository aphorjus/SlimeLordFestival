package game.client.states;

import game.*;
import game.client.Board;
import game.client.GameClient;
import game.server.GameServer;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.lwjgl.opengl.EXTAbgr;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverworldState extends BasicGameState {
    InputManager inputManager;
    TextField textField;
    GameApi gameApi;
    GameClient gameClient;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        gameApi = new GameApi((GameClient)sbg);
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

        gameApi.update();
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

    void apiTest() {
        gameClient.sendMessage("this is a test message");
    }

}
