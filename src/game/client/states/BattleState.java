package game.client.states;

import game.IGameState;
import game.api.GameApi;
import game.api.GameApiListener;
import game.client.Player;
import game.entities.IEntity;
import jig.Vector;
import game.Battles.BattleGrid;
import game.Battles.BattleGridTile;
import game.InputManager;
import game.client.GameClient;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class BattleState extends BasicGameState implements GameApiListener {
    InputManager inputManager;
    BattleGrid battleGrid;
    GameApi gameApi;
    GameClient gameClient;

    public static int[][] PLAIN_MAP =
            {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,2,1,1,1,1,1,1,2,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        GameClient game = (GameClient)sbg;
        gameClient = (GameClient)sbg;
        inputManager = game.inputManager;
        gameApi = new GameApi((GameClient) sbg, this);

        this.battleGrid = new BattleGrid(game.ScreenHeight, game.ScreenWidth,
                30, gameApi, BattleState.PLAIN_MAP);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        this.gameApi = new GameApi(gameClient, this);
        this.battleGrid.setGameApi(gameApi);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        Input input = gc.getInput();

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
            this.battleGrid.selectTile(mousePosition);

        }
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
//            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
//            this.battleGrid.selectTile(mousePosition);
            this.battleGrid.deselectTile();
        }

        gameApi.update();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        battleGrid.render(g);
    }

    @Override
    public int getID() {
        return GameClient.BATTLE_STATE;
    }

    public void onAlterGameState(IGameState gameState) { }

    public void onAlterPlayerState(Player player) {}

    public void onCreateEntity(IEntity entity) {

        BattleGridTile tile = (BattleGridTile) entity;
        System.out.println(tile.occupent.toJson());
        battleGrid.replaceTile(tile);

    }

    public void onDeleteEntity(int entityId) {}

    public void onMessage(int senderId, String message) {}

    public void onSetStateToBattle() {}

    public void onSetStateToOverworld() {}

    public void onEndTurn() {}

    public void onLobbyClientListUpdate(String[] clientNames) {}

    public void onLobbyIsFull() {}
}
