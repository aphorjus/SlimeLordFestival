package game.client.states;

import game.IGameState;
import game.InputManager;
import game.api.GameApi;
import game.api.GameApiListener;
import game.client.GameClient;
import game.client.IPlayerState;
import game.entities.IEntity;
import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import game.client.Board;
import org.newdawn.slick.Image;
import game.client.Button;
import org.w3c.dom.Text;

public class StartUpState extends BasicGameState implements GameApiListener {
    InputManager inputManager;
    public static final String TITLE = "game/client/resource/title.png";
    public static final String JOINGAME = "game/client/resource/JoinGame.png";
    public static final String HOSTGAME = "game/client/resource/HostGame.png";
    Button joinButton = null;
    Button hostButton = null;
    GameClient gameClient;
    GameApi gameApi;
    TextField ipAdd;
    TextField portNum;
    boolean failedConnect;
    int state = 0; // 0 is title screen, 1 is HostGame screen, 2 is JoinGame screen

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        GameClient game = (GameClient)sbg;
        gameClient = (GameClient)sbg;
        inputManager = game.inputManager;
        ResourceManager.loadImage(TITLE);
        ResourceManager.loadImage(HOSTGAME);
        ResourceManager.loadImage(JOINGAME);
        Image joinGame = new Image(JOINGAME);
        Image hostGame = new Image(HOSTGAME);
        joinButton = new Button(60,440,joinGame);
        hostButton = new Button(640,440,hostGame);
        gameApi = new GameApi((GameClient) sbg, this);
        failedConnect = false;

        //Creating text field for IP address
        ipAdd = new TextField(gc, gc.getDefaultFont(), 300, 130, 150, 40);
        ipAdd.setBackgroundColor(Color.darkGray);
        ipAdd.setBorderColor(Color.white);
        ipAdd.setTextColor(Color.white);
        ipAdd.setMaxLength(253);
        ipAdd.setFocus(true);
        ipAdd.isAcceptingInput();

        //Creating text field for port number
        portNum = new TextField(gc, gc.getDefaultFont(), 300, 200, 150, 40);
        portNum.setBackgroundColor(Color.darkGray);
        portNum.setBorderColor(Color.white);
        portNum.setTextColor(Color.white);
        portNum.setMaxLength(253);
        portNum.setFocus(true);
        portNum.isAcceptingInput();
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        // Required to update before game process
        inputManager.update();
        Input input = gc.getInput();
        GameClient bg = (GameClient)sbg;

        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            int mx = input.getMouseX();
            int yx = input.getMouseY();
            System.out.println("Mouse X:" + input.getMouseX() + " Mouse Y:" + input.getMouseY());

            //If host game button gets clicked switch to host state
            if(hostButton.checkClick(mx,yx) == true && state == 0){
                System.out.println("Host Game button clicked");
                state = 1;
                //bg.enterState(bg.PLAYING_STATE);
            }
            //If join game button gets clicked switch to join game
            if(joinButton.checkClick(mx,yx) == true && state == 0){
                System.out.println("Join Game button clicked");
                state = 2;
                //bg.enterState(bg.PLAYING_STATE);
            }
        }

        //if enter is inputted on Join Game try to join host at IP
        if(input.isKeyPressed(input.KEY_ENTER) && state == 2){
            System.out.println("Attempting to join: " + ipAdd.getText() + " with port number: " + portNum.getText());
            try{
                gameClient.connectToServer(ipAdd.getText(),Integer.parseInt(portNum.getText()));
            }catch (Exception e) {
                e.printStackTrace();
                failedConnect = true;
            }
            //gameApi.update();
            //gameApi.setGameState(GameApi.SetGameStateOverworld);
        }

        if(input.isKeyPressed(Input.KEY_ESCAPE)){
            state = 0;
            failedConnect = false;
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;

        if (state == 0){
            g.drawImage(ResourceManager.getImage(TITLE),
                    0, 0);
            joinButton.render(g);
            hostButton.render(g);
        }else if(state == 1){
            g.drawString("You are Hosting a game at 192.181.1.3",300,83);
        }else if(state == 2){
            g.drawString("Enter IP address and portnumber then press enter to join game",300,83);
            g.drawString("IP ADDRESS:",200,130);
            g.drawString("PORT NUMBER:",190,200);
            ipAdd.render(gc,g);
            portNum.render(gc,g);
            if(failedConnect == true){
                g.drawString("Could not connect please try again",300,40);
            }
        }

    }

    @Override
    public int getID() {
        return GameClient.STARTUP_STATE;
    }

    public void onAlterGameState(IGameState gameState) { }

    public void onAlterPlayerState(IPlayerState playerState) {}

    public void onCreateEntity(IEntity entity) {}

    public void onDeleteEntity(int entityId) {}

    public void onMessage(int senderId, String message) { }

    public void onSetStateToBattle() {}

    public void onSetStateToOverworld() {
        System.out.println("setting overworld");
        gameClient.enterState(GameClient.OVERWORLD_STATE);
    }
}
