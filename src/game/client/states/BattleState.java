package game.client.states;

import game.Battles.*;
import game.IGameState;
import game.api.GameApi;
import game.api.GameApiListener;
import game.client.Player;
import game.entities.IEntity;
import game.entities.slime.Slime;
import game.entities.slimefactory.SlimeFactory;
import game.entities.slimelord.SlimeLord;
import jig.ResourceManager;
import jig.Vector;
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
    Music battleMusic = null;
    SlimBox slimeBox = new SlimBox();
    Boolean showHelp = null;
    Image helpMenu = null;
    Image controls = null;
    int currentAbility = 0;
    int playerOne;
    int playerTwo;
    int activePlayer;
    int winner;


    public static int[][] PLAIN_MAP =
            {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
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
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {

        GameClient game = (GameClient)sbg;
        gameClient = (GameClient)sbg;
        inputManager = game.inputManager;
        gameApi = new GameApi((GameClient) sbg, this);
        try{
            battleMusic = new Music("game/client/resource/battle.wav");
            this.helpMenu = new Image("game/client/resource/LobbyBoard.png");
            this.controls = new Image("game/client/resource/Controls.png");
        }catch (Exception e){
            e.printStackTrace();
        }

        this.battleGrid = new BattleGrid((int)(game.ScreenHeight/1.2), game.ScreenWidth,
                30, gameApi, BattleState.PLAIN_MAP);
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        this.gameApi = new GameApi(gameClient, this);
        this.battleGrid.setGameApi(gameApi);
        this.winner = -1;
        this.showHelp = false;
        battleMusic.loop();
//        System.out.println(gameClient.myId);
        //TEIMP

//        this.slimeLordOne = new SlimeLord(0);
//        this.slimeLordTwo = new SlimeLord(1);
//
//        this.slimeLordOne.addAbility("summonBasicSlime");
//        this.slimeLordOne.addAbility("slimeBall");
////        this.slimeLordOne.addAbility("damage");
//
//        this.slimeLordTwo.addAbility("slimeStrike");
//        this.slimeLordTwo.addAbility("summonLancer");
//
//        this.slimeLordOne.specialSlimes.add("lancer");
//        this.slimeLordOne.specialSlimes.add("striker");
//        this.slimeLordOne.specialSlimes.add("advancedStriker");
//        this.slimeLordOne.specialSlimes.add("advancedLancer");
//
//        this.slimeLordTwo.specialSlimes.add("lancer");
//        this.slimeLordTwo.specialSlimes.add("striker");
//        this.slimeLordTwo.specialSlimes.add("advancedStriker");
//        this.slimeLordTwo.specialSlimes.add("advancedLancer");
//
//        spawnInFactories();
//
//        playerOne = slimeLordOne.clientID;
//        playerTwo = slimeLordTwo.clientID;
//        activePlayer = playerOne;
//        activeSlimeLord = slimeLordOne;

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

            if(slimeBox.isActive()){
                slimeBox.update(input.getMouseX(), input.getMouseY());
                slimeBox.setActive(false);
            }

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
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
            this.battleGrid.deselectTile();

            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
            BattleEntity occupent = battleGrid.getTile(mousePosition).getOccupent();

            if (occupent instanceof Slime) {
                slimeBox.updateBox((Slime) occupent, activeSlimeLord);
                slimeBox.setActive(true);
            }
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
        if ( input.isKeyPressed(Input.KEY_1) && isMyTurn()){
            battleGrid.enterAblityMode(activeSlimeLord.getAbility(0));
            this.currentAbility = 0;
        }
        if ( input.isKeyPressed(Input.KEY_2) && isMyTurn()){
            battleGrid.enterAblityMode(activeSlimeLord.getAbility(1));
            this.currentAbility = 1;
        }
        if ( input.isKeyPressed(Input.KEY_3) && isMyTurn()){
            battleGrid.enterAblityMode(activeSlimeLord.getAbility(2));
            this.currentAbility = 2;
        }
        if (input.isKeyDown(Input.KEY_C)){
            this.showHelp = true;
        }else{
            this.showHelp = false;
        }
        battleGrid.update(delta);
        gameApi.update();

        if( battleGrid.getWinner() != winner ){
            winner = battleGrid.getWinner();
            // DO SOMETHING?!?
            // Enter overworld and tell it who won somehow
            //
            gameClient.setBattleStateWinner(winner);
            gameClient.enterState(GameClient.OVERWORLD_STATE);
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

    public void displayBasicInfo(Graphics g, BattleGridTile tile){

        if( tile.hasOccupent() ){

            HealthBar healthBar = new HealthBar(30, 5, tile.getOccupent());
            healthBar.render(g);

            if( tile.getOccupent() instanceof Slime && ((Slime) tile.getOccupent()).onCooldown() ) {

                String cooldown = String.valueOf(((Slime) tile.getOccupent()).getCurrentCooldown());
                g.drawString(cooldown, healthBar.xpos-9, healthBar.ypos-7);
            }
        }

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        Input input = gc.getInput();
        g.setBackground(Color.green);
        battleGrid.render(g);

        Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
        if(this.battleGrid.getTile(mousePosition) != null){
            this.battleGrid.mouseoverHighlight(mousePosition, g);
            displayBasicInfo(g, battleGrid.getTile(mousePosition));
        }
        g.setColor(Color.white);
        g.drawString("Current Mode:",375,385);
        if(isMyTurn() == false){
            g.setColor(Color.orange);
            g.drawString("Enemies Turn",495,385);
        }else if(battleGrid.mode == 1){
            g.setColor(Color.green);
            g.drawString("Movement Mode",495,385);
        }else if(battleGrid.mode == 2){
            g.setColor(Color.red);
            g.drawString("Attack Mode",495,385);
        }else{
            g.drawString("Current Ability:",375,410);
            g.setColor(Color.blue);
            g.drawString("Ability Mode",495,385);
            g.setColor(Color.cyan);
            g.drawString(activeSlimeLord.getAbility(this.currentAbility),520,410);
        }

        g.setColor(Color.white);
        g.drawString("(Hold \"C\" to view controls)",375,480);
        if(this.showHelp == true){

            g.drawImage(helpMenu,0,0);
            g.drawImage(controls,365,107);
            g.drawString("Press \"E\" to end your turn.",250,150);
            g.drawString("Press \"S\" to switch between attack mode and move mode.",250,180);
            g.drawString("Press \"1\",\"2\",or \"3\" to access different abilities",250,210);
            g.drawString("Right click a slime to upgrade it",250,240);
            g.drawString("Left click a slime to move/attack (based on current mode)",250,270);
        }
        slimeBox.render(g);
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

        this.playerOne = slimeLordOne.clientID;
        this.playerTwo = slimeLordTwo.clientID;

        this.activeSlimeLord = slimeLordOne;
        this.activePlayer = slimeLordOne.clientID;

        spawnInFactories();
    }

    public void onSetStateToOverworld() {}

    public void onEndTurn() {
        endTurn();
    }

    public void onLobbyClientListUpdate(String[] clientNames) {}

    public void onLobbyIsFull() {}

    public void onConnectionConfirmation(int myId) { }
}
