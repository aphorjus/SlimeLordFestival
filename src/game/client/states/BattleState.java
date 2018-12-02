package game.client.states;

import game.IGameState;
import game.api.GameApi;
import game.api.GameApiListener;
import game.client.Player;
import game.entities.IEntity;
import game.entities.slime.Slime;
import game.entities.slimefactory.SlimeFactory;
import game.entities.slimelord.SlimeLord;
import jig.Vector;
import game.Battles.BattleGrid;
import game.Battles.BattleGridTile;
import game.InputManager;
import game.client.GameClient;
import org.json.JSONObject;
import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

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

        this.battleGrid = new BattleGrid((int)(game.ScreenHeight/1.2), game.ScreenWidth,
                30, gameApi, BattleState.PLAIN_MAP);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        this.gameApi = new GameApi(gameClient, this);
        this.battleGrid.setGameApi(gameApi);
    }

    public void startTurn(){
        ArrayList<IEntity> entitys = battleGrid.getEntityList();

        for( int i = 0; i < entitys.size(); i++ ) {

            if (entitys.get(i) instanceof SlimeFactory) {
                ((SlimeFactory) entitys.get(i)).onNextTurn();
            }
        }
    }

    public void endTurn(){
        ArrayList<IEntity> entitys = battleGrid.getEntityList();

        for( int i = 0; i < entitys.size(); i++ ){

            if( entitys.get(i) instanceof Slime ){// && ((Slime) entitys.get(i)).clientID == GameClient.id){
                ((Slime) entitys.get(i)).onNextTurn();
            }
        }

    }


    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        Input input = gc.getInput();

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
            this.battleGrid.selectTile(mousePosition);
        }
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
            this.battleGrid.deselectTile();
        }

        if (input.isKeyPressed(Input.KEY_E)){
//            endTurn();
            gameApi.endTurn();
        }

        gameApi.update();
    }

    public void displayCoolDown(Graphics g, BattleGridTile tile){

        if(tile.hasOccupent() && tile.getOccupent() instanceof Slime){

            String cooldown = String.valueOf(((Slime) tile.getOccupent()).getCooldownRemaining());

            g.drawString( cooldown, (int)tile.getPosition().getX(), (int)tile.getPosition().getY());
        }
    }


    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        Input input = gc.getInput();

        g.setBackground(Color.green);

        battleGrid.render(g);

        g.setColor(Color.black);

        Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());

        if(this.battleGrid.getTile(mousePosition) != null){
            if(this.battleGrid.getTile(mousePosition).hasOccupent()) {
                displayCoolDown(g, this.battleGrid.getTile(mousePosition));
            }
        }
    }

    @Override
    public int getID() {
        return GameClient.BATTLE_STATE;
    }

    public void onAlterGameState(IGameState gameState) { }

    public void onAlterPlayerState(Player player) {}

    public void onCreateEntity(IEntity entity) {
        BattleGridTile tile = (BattleGridTile) entity;
        System.out.println(tile.toJson());
        battleGrid.replaceTile(tile);
    }

    public void onDeleteEntity(int entityId) {}

    public void onMessage(int senderId, String message) {}

    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {}

    public void onSetStateToOverworld() {}

    public void onEndTurn() {
        endTurn();
    }

    public void onLobbyClientListUpdate(String[] clientNames) {}

    public void onLobbyIsFull() {}
    public void onConnectionConfirmation(int myId) { }
}
