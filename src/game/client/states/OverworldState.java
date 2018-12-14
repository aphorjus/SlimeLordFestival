package game.client.states;

import game.*;
import game.api.GameApi;
import game.client.Board;
import game.api.GameApiListener;
import game.client.Button;
import game.client.GameClient;
import game.client.Player;
import game.entities.IEntity;
import game.entities.building.Shop;
import game.entities.slimelord.SlimeLord;
import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class OverworldState extends BasicGameState implements GameApiListener {
    String GREEN_SLIMELORD_IDLE = "game/client/resource/slime-lord-green.png";
    String BLUE_SLIMELORD_IDLE = "game/client/resource/slime-lord-blue.png";
    String YELLOW_SLIMELORD_IDLE = "game/client/resource/slime-lord-yellow.png";
    String RED_SLIMELORD_IDLE = "game/client/resource/slime-lord-red.png";

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

    InputManager inputManager;
    TextField textField;
    GameApi gameApi;
    GameClient gameClient;
    Shop currentShop = null;
    boolean inShop = false;
    Button endButton;

    private Board board;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        gameClient = (GameClient)sbg;
        gameApi = new GameApi((GameClient)sbg, this);
        inputManager = gameClient.inputManager;

        try {
            endButton = new Button(950, 450, new Image("game/client/resource/end-button.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
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

        gameApi = new GameApi((GameClient)sbg, this);
        GameClient bg = (GameClient)sbg;
        currentShop = new Shop(bg);
        Board board = bg.getBoard();
        board.setUp(gameApi, gameClient);
        this.board = board;
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        // Required to update before game process
        inputManager.update();
        Input input = gc.getInput();
        GameClient bg = (GameClient)sbg;
        Board board = bg.getBoard();
        // board.setUp(gameApi, gameClient);
        board.updateSlimelord();
        // board.showHighlightedPaths(input.getMouseX(), input.getMouseY());

        /*
        if(input.isMousePressed(input.MOUSE_LEFT_BUTTON)){
            board.moveTo(input.getMouseX(), input.getMouseY());
        }
        */

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
        if (input.isKeyDown(Input.KEY_X)) {
            SlimeLord testSlimeLord = new SlimeLord(0);
            currentShop.setCurrentSlimeLord(testSlimeLord);
            inShop = true;
        }

        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            if(inShop == true){
                currentShop.checkClick(input.getMouseX(), input.getMouseY());
            } else if (gameClient.myId == board.turn.turnID && endButton.checkClick(input.getMouseX(), input.getMouseY())) {
                gameApi.endTurn();
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
        }

        if (!inShop && gameClient.myId == board.turn.turnID) {
            endButton.render(g);
        }
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
        board.onDeleteEntity(entityId);
    }

    public void onMessage(int senderId, String message) {
    }

    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {}

    public void onSetStateToOverworld() {}

    public void onEndTurn() {
        System.out.println("end turn detected.");
        board.endTurn();
    }

    public void onLobbyClientListUpdate(String[] clientNames) {}
    public void onLobbyIsFull() {}
    public void onConnectionConfirmation(int myId) {}

}
