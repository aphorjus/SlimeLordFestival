package game.client.states;

import game.InputManager;
import game.client.GameClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import game.client.Board;

public class StartUpState extends BasicGameState {
    InputManager inputManager;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        GameClient game = (GameClient)sbg;
        inputManager = game.inputManager;
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        sbg.enterState(GameClient.OVERWORLD_STATE);
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

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        Board board = bg.getBoard();
        board.render(gc, sbg, g);
    }

    @Override
    public int getID() {
        return GameClient.STARTUP_STATE;
    }
}
