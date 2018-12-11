package game.client.states;

import game.Battles.BattleEntity;
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
import java.util.LinkedList;

public class BattleState extends BasicGameState implements GameApiListener {
    InputManager inputManager;
    BattleGrid battleGrid;
    GameApi gameApi;
    GameClient gameClient;
    SlimeLord slimeLordOne;
    SlimeLord slimeLordTwo;
    SlimeLord activeSlimeLord;

    int playerOne;
    int playerTwo;
    int activePlayer;
    int winner;



    public static int[][] PLAIN_MAP =
            {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,2,1,1,1,1,1,1,2,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};


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
        this.winner = -1;
//        System.out.println(gameClient.myId);
        //TEIMP

        this.slimeLordOne = new SlimeLord(0);
        this.slimeLordTwo = new SlimeLord(1);

        this.slimeLordOne.addAbility("summonBasicSlime");
        this.slimeLordOne.addAbility("slimeBall");
        this.slimeLordOne.addAbility("damage");

        this.slimeLordTwo.addAbility("slimeStrike");
        this.slimeLordTwo.addAbility("summonLancer");

        spawnInFactories();

        playerOne = slimeLordOne.clientID;
        playerTwo = slimeLordTwo.clientID;
        activePlayer = playerOne;
        activeSlimeLord = slimeLordOne;

        //END TEMP

    }

    public boolean isMyTurn(){
        return activePlayer == gameClient.myId;
    }

    public void spawnInFactories(){

        int x = 1;
        int y;
        SlimeLord lord = slimeLordOne;

        for( int i = 0; i < lord.factories.size(); i++ ){
            y = ( battleGrid.gridHeight/(lord.factories.size()+1) )*(i+1);
            battleGrid.addOccupentTo(x, y, lord.factories.get(i));
        }

        x = battleGrid.gridWidth-2;
        lord = slimeLordTwo;

        for( int i = 0; i < lord.factories.size(); i++ ){
            y = ( battleGrid.gridHeight/(lord.factories.size()+1) )*(i+1);
            battleGrid.addOccupentTo(x, y, lord.factories.get(i));
        }

    }

    public void endTurn(){
        LinkedList<BattleEntity> entitys = battleGrid.getEntityList();

        for( int i = 0; i < entitys.size(); i++ ){

            if( entitys.get(i) instanceof Slime && ((Slime) entitys.get(i)).clientID == activePlayer){
                entitys.get(i).onNextTurn();
            }
            if( entitys.get(i) instanceof SlimeFactory && ((SlimeFactory) entitys.get(i)).clientID != activePlayer){
                BattleGridTile tile = ((SlimeFactory) entitys.get(i)).spawnSlime();
                if( tile != null ) {
                    gameApi.createEntity(tile);
                }
            }
        }
        if (activePlayer == playerOne){
            activePlayer = playerTwo;
            activeSlimeLord = slimeLordTwo;
        }
        else{
            activePlayer = playerOne;
            activeSlimeLord = slimeLordOne;
        }
        System.out.println("activePlay: " + activePlayer);
        battleGrid.ability.onEndTurn(activePlayer);
        battleGrid.setMode(BattleGrid.MOVMENT_MODE);
    }


    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        Input input = gc.getInput();

        if ( input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ){
            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
            if( battleGrid.tileSelected()){
                if( activePlayer != gameClient.myId ||
                        ((Slime)battleGrid.getSelectedTile().getOccupent()).clientID != gameClient.myId ){
                    this.battleGrid.deselectTile();
                }
                else {
                    this.battleGrid.selectTile( mousePosition );
                }
            }
            else {
                this.battleGrid.selectTile(mousePosition);
            }
        }
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
            this.battleGrid.deselectTile();
        }

//        if (input.isKeyPressed(Input.KEY_E) && isMyTurn()){
//            gameApi.endTurn();
//        }

        if (input.isKeyPressed(Input.KEY_E)){
            gameApi.endTurn();
        }

        if (input.isKeyPressed(Input.KEY_S)){
            battleGrid.switchMode();
        }
        if ( input.isKeyPressed(Input.KEY_1) ){// && isMyTurn()){
            battleGrid.enterAblityMode(activeSlimeLord.getAbility(0));
        }
        if ( input.isKeyPressed(Input.KEY_2) ){// && isMyTurn()){
            battleGrid.enterAblityMode(activeSlimeLord.getAbility(1));
        }
        if ( input.isKeyPressed(Input.KEY_3) ){// && isMyTurn()){
            battleGrid.enterAblityMode(activeSlimeLord.getAbility(2));
        }
        battleGrid.update(delta);
        gameApi.update();

        if( battleGrid.getWinner() != winner ){
            winner = battleGrid.getWinner();
            // DO SOMETHING?!?
            // Enter overworld and tell it who won somehow
            //
        }
    }

    public void displayCoolDown(Graphics g, BattleGridTile tile){

        if(tile.hasOccupent() && tile.getOccupent() instanceof Slime){
            if( ((Slime) tile.getOccupent()).onCooldown() ) {

                String cooldown = String.valueOf(((Slime) tile.getOccupent()).getCurrentCooldown());
                g.drawString(cooldown, (int) tile.getPosition().getX(), (int) tile.getPosition().getY());
            }
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        Input input = gc.getInput();

        g.setBackground(Color.green);
        battleGrid.render(g);

//        g.setColor(Color.black);
        Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());

        if(this.battleGrid.getTile(mousePosition) != null){
            this.battleGrid.mouseoverHighlight(mousePosition, g);
            if( this.battleGrid.getTile(mousePosition).hasOccupent() ) {
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

        if(entity instanceof BattleGridTile) {
            BattleGridTile tile = (BattleGridTile) entity;
            battleGrid.replaceOccupent(tile);
        }
        else if (entity instanceof BattleEntity){
            battleGrid.getTile(((Slime) entity).getPosition()).setOccupent((BattleEntity) entity);
        }
    }

    public void onDeleteEntity(int entityId) {

        LinkedList<BattleEntity> entities = battleGrid.getEntityList();

        for(int i = 0; i < entities.size(); i++){
//            if( ((IEntity)entities.get(i)).id.equals(entityId) ){
//
//            }
        }

    }

    public void onMessage(int senderId, String message) {}

    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {

        this.slimeLordOne = lordOne;
        this.slimeLordTwo = lordTwo;

        spawnInFactories();

        this.playerOne = slimeLordOne.clientID;
        this.playerTwo = slimeLordTwo.clientID;

        this.activePlayer = slimeLordOne.clientID;
    }

    public void onSetStateToOverworld() {}

    public void onEndTurn() {
        endTurn();
    }

    public void onLobbyClientListUpdate(String[] clientNames) {}

    public void onLobbyIsFull() {}

    public void onConnectionConfirmation(int myId) { }
}
