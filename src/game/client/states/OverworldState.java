package game.client.states;

import game.*;
import game.api.GameApi;
import game.client.Board;
import game.api.GameApiListener;
import game.client.GameClient;
import game.client.Player;
import game.entities.IEntity;
import game.entities.slimelord.SlimeLord;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverworldState extends BasicGameState implements GameApiListener {
    InputManager inputManager;
    TextField textField;
    GameApi gameApi;
    GameClient gameClient;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        gameClient = (GameClient)sbg;
        gameApi = new GameApi((GameClient)sbg, this);
        inputManager = gameClient.inputManager;
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {

        gameApi = new GameApi((GameClient)sbg, this);
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

    public void onAlterGameState(IGameState gameState) { }

    public void onAlterPlayerState(Player player) {}

    public void onCreateEntity(IEntity entity) {}

    public void onDeleteEntity(int entityId) {}

    public void onMessage(int senderId, String message) {
        System.out.println(senderId);
        System.out.println(message);
    }

    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {}

    public void onSetStateToOverworld() {}

    public void onEndTurn() {}

    public void onLobbyClientListUpdate(String[] clientNames) {}
    public void onLobbyIsFull() {}
    public void onConnectionConfirmation(int myId) {}

}
