package game.client.states;

import game.*;
import game.api.GameApi;
import game.client.Board;
import game.api.GameApiListener;
import game.client.Button;
import game.client.GameClient;
import game.client.Player;
import game.entities.IEntity;
import game.entities.TokenAnimation;
import game.entities.building.Shop;
import game.entities.slimelord.SlimeLord;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverworldState extends BasicGameState implements GameApiListener {
    String GREEN_SLIMELORD_IDLE = "game/client/resource/slime-lord-green.png";
    String BLUE_SLIMELORD_IDLE = "game/client/resource/slime-lord-blue.png";
    String YELLOW_SLIMELORD_IDLE = "game/client/resource/slime-lord-yellow.png";
    String RED_SLIMELORD_IDLE = "game/client/resource/slime-lord-red.png";
    Music overworldMusic = null;
    Music shopMusic = null;
    String GREEN_IDLE = "game/client/resource/green-slime-idle.png";
    String GREEN_ATTACK = "game/client/resource/green-slime-attack.png";
    String GREEN_DEATH = "game/client/resource/green-slime-death.png";

    String RED_IDLE = "game/client/resource/red-slime-idle.png";
    String RED_ATTACK = "game/client/resource/red-slime-attack.png";
    String RED_DEATH = "game/client/resource/red-slime-death.png";

    String YELLOW_IDLE = "game/client/resource/yellow-slime-idle.png";
    String YELLOW_ATTACK = "game/client/resource/yellow-slime-attack.png";
    String YELLOW_DEATH = "game/client/resource/yellow-slime-death.png";

    String BLUE_IDLE = "game/client/resource/blue-slime-idle.png";
    String BLUE_ATTACK = "game/client/resource/blue-slime-attack.png";
    String BLUE_DEATH = "game/client/resource/blue-slime-death.png";

    String FIGHT_POPUP = "game/client/resource/fight-popup.png";

    InputManager inputManager;
    TextField textField;
    GameApi gameApi;
    GameClient gameClient;
    Shop currentShop = null;
    boolean inShop = false;
    Button endButton;
    Button exitButton;
    TokenAnimation tokenAnimation = new TokenAnimation(new Vector(25, 480));

    boolean alreadySetUp = false;
    private Board board;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        gameClient = (GameClient)sbg;
        gameApi = new GameApi((GameClient)sbg, this);
        inputManager = gameClient.inputManager;

        try {
            overworldMusic = new Music("game/client/resource/Caketown1.wav");
            shopMusic = new Music("game/client/resource/blanchet.wav");
            endButton = new Button(950, 450, new Image("game/client/resource/end-button.png"));
            exitButton = new Button(700, 430, new Image("game/client/resource/ExitShop.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        overworldMusic.loop();
        ResourceManager.loadImage(GREEN_SLIMELORD_IDLE);
        ResourceManager.loadImage(BLUE_SLIMELORD_IDLE);
        ResourceManager.loadImage(YELLOW_SLIMELORD_IDLE);
        ResourceManager.loadImage(RED_SLIMELORD_IDLE);

        ResourceManager.loadImage(GREEN_IDLE);
        ResourceManager.loadImage(GREEN_ATTACK);
        ResourceManager.loadImage(GREEN_DEATH);

        ResourceManager.loadImage(RED_IDLE);
        ResourceManager.loadImage(RED_ATTACK);
        ResourceManager.loadImage(RED_DEATH);

        ResourceManager.loadImage(YELLOW_IDLE);
        ResourceManager.loadImage(YELLOW_ATTACK);
        ResourceManager.loadImage(YELLOW_DEATH);

        ResourceManager.loadImage(BLUE_IDLE);
        ResourceManager.loadImage(BLUE_ATTACK);
        ResourceManager.loadImage(BLUE_DEATH);

        ResourceManager.loadImage(FIGHT_POPUP);

        gameApi = new GameApi((GameClient)sbg, this);
        GameClient bg = (GameClient)sbg;
        currentShop = new Shop(bg,gameApi);
        Board board = bg.getBoard();

        if (!alreadySetUp) {
            board.setUp(gameApi, gameClient);
        }

        alreadySetUp = true;
        this.board = board;

        if (gameClient.battleStateWinner != -1 && gameClient.battleStateWinner == gameClient.myId) {
            gameClient.battleStateWinner = -1;
            battleWon();
        }

        if (gameClient.battleStateLoser != -1) {
            removeLoser(gameClient.battleStateLoser);
            gameClient.battleStateLoser = -1;
        }
    }

    void removeLoser(int loserId) {
        if (loserId == -1) return;

        board.turn.addLoser(loserId);
        board.removeLoser(loserId);

        boolean iAmALoser = false;

        for (int i : board.turn.loserIds) {
            if (i == gameClient.myId) iAmALoser = true;
        }

        if (iAmALoser) callMeALoser();

        if (!iAmALoser && board.turn.loserIds.size() == gameClient.players.length - 1) {
            callMeAWinner();
        }
    }

    void callMeALoser() {
        // This is where you set all the logic to call the player a loser
        gameClient.enterState(GameClient.STARTUP_STATE);
    }

    void callMeAWinner() {
        // this is wher you set all the logic to call the player a winner
        gameClient.enterState(GameClient.STARTUP_STATE);
    }

    void battleWon() {
        gameClient.setTokens(gameClient.getTokens() + 600);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        // Required to update before game process
        inputManager.update();
        Input input = gc.getInput();
        GameClient bg = (GameClient)sbg;
        Board board = bg.getBoard();
        // board.setUp(gameApi, gameClient);
        // board.showHighlightedPaths(input.getMouseX(), input.getMouseY());


        board.update(delta, input);
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
            BattleState.testBattle = true;
            bg.enterState(GameClient.BATTLE_STATE);
        }
        
        if (input.isKeyDown(Input.KEY_X)) {
            SlimeLord shopSlimeLord = null;
            for (int i = 0; i < board.slimeLords.size(); i++) {
                if(board.slimeLords.get(i).clientID == gameClient.myId){
                    shopSlimeLord = board.slimeLords.get(i);
                    break;
                }
            }
            currentShop.setCurrentSlimeLord(shopSlimeLord);
            currentShop.setAPI(gameApi);
            inShop = true;
            overworldMusic.pause();
            shopMusic.loop();
        }

        if (input.isKeyDown(Input.KEY_P)){
            currentShop.viewPrices = true;
        }else{
            currentShop.viewPrices = false;
        }


        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            if(inShop == true){
                currentShop.checkClick(input.getMouseX(), input.getMouseY());
                if(exitButton.checkClick(input.getMouseX(),input.getMouseY())){
                    inShop = false;
                    currentShop.exitShop();
                    shopMusic.stop();
                    overworldMusic.loop();
                }
            } else if (gameClient.myId == board.turn.turnID &&
                    endButton.checkClick(input.getMouseX(), input.getMouseY())) {
                gameApi.endTurn();
            } else {
                board.click(input.getMouseX(), input.getMouseY());
            }
        }

        gameApi.update();
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        Board board = bg.getBoard();
        board.render(gc, sbg, g);

        if (inShop == true){
            currentShop.render(g);
            exitButton.render(g);
        }

        if (!inShop && gameClient.myId == board.turn.turnID) {
            endButton.render(g);
        }

        tokenAnimation.render(g);
        g.drawString(gameClient.getTokens() + "", 40, 470);
    }

    @Override
    public int getID() {
        return GameClient.OVERWORLD_STATE;
    }

    public void onAlterGameState(IGameState gameState) { }

    public void onAlterPlayerState(Player player) {}

    public void onCreateEntity(IEntity entity) {
        board.onCreateEntity(entity);
    }

    public void onDeleteEntity(int entityId) {
    }

    public void onMessage(int senderId, String message) {
    }

    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {
        gameClient.startBattle(lordOne, lordTwo);
    }

    public void onSetStateToOverworld() {}

    public void onEndTurn() {
        System.out.println("end turn detected.");
        board.endTurn(gameClient);
    }

    public void onLobbyClientListUpdate(String[] clientNames) {}
    public void onLobbyIsFull() {}
    public void onConnectionConfirmation(int myId) {}

}
