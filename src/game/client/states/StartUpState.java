package game.client.states;

import game.InputManager;
import game.client.GameClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class StartUpState extends BasicGameState {
    InputManager inputManager;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        GameClient game = (GameClient)sbg;
        inputManager = game.inputManager;
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        sbg.enterState(GameClient.PLAYING_STATE);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        // Required to update before game process
        inputManager.update();

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) {

    }

    @Override
    public int getID() {
        return GameClient.STARTUP_STATE;
    }
}
