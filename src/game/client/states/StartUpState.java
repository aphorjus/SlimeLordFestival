package game.client.states;

import game.IGameState;
import game.InputManager;
import game.api.GameApi;
import game.api.GameApiListener;
import game.client.GameClient;
import game.client.Player;
import game.entities.IEntity;
import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Image;
import game.client.Button;

public class StartUpState extends BasicGameState implements GameApiListener {
    InputManager inputManager;
    public static final String TITLE = "game/client/resource/title.png";
    public static final String JOINGAME = "game/client/resource/JoinGame.png";
    public static final String HOSTGAME = "game/client/resource/HostGame.png";
    public static final String STARTGAME = "game/client/resource/StartGame.png";
    Button joinButton = null;
    Button hostButton = null;
    Button startButton = null;
    GameClient gameClient;
    GameApi gameApi;
    TextField ipAdd;
    TextField portNum;
    TextField playerNumb;
    boolean failedConnect;
    boolean connected;
    boolean host;
    String[] clientList;
    int state = 0; // 0 is title screen, 1 is HostGame screen, 2 is JoinGame screen, 3 is join lobby

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        GameClient game = (GameClient)sbg;
        gameClient = (GameClient)sbg;
        inputManager = game.inputManager;
        ResourceManager.loadImage(TITLE);
        ResourceManager.loadImage(HOSTGAME);
        ResourceManager.loadImage(JOINGAME);
        ResourceManager.loadImage(STARTGAME);
        Image joinGame = new Image(JOINGAME);
        Image hostGame = new Image(HOSTGAME);
        Image startGame = new Image(STARTGAME);
        joinButton = new Button(60,440,joinGame);
        hostButton = new Button(640,440,hostGame);
        startButton = new Button(640,40,startGame);
        gameApi = new GameApi((GameClient) sbg, this);
        failedConnect = false;
        connected = false;
        host = false;

        //Creating text field for IP address
        ipAdd = new TextField(gc, gc.getDefaultFont(), 300, 130, 150, 40);
        ipAdd.setBackgroundColor(Color.darkGray);
        ipAdd.setBorderColor(Color.white);
        ipAdd.setTextColor(Color.white);
        ipAdd.setMaxLength(253);
        ipAdd.setFocus(true);
        ipAdd.isAcceptingInput();

        //Creating text field for Number of Players
        playerNumb = new TextField(gc, gc.getDefaultFont(), 300, 330, 150, 40);
        playerNumb.setBackgroundColor(Color.darkGray);
        playerNumb.setBorderColor(Color.white);
        playerNumb.setTextColor(Color.white);
        playerNumb.setMaxLength(253);
        playerNumb.setFocus(true);
        playerNumb.isAcceptingInput();

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
        if (connected == true){
            gameApi.update();
        }

        if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            int mx = input.getMouseX();
            int my = input.getMouseY();
            System.out.println("Mouse X:" + input.getMouseX() + " Mouse Y:" + input.getMouseY());

            //If host game button gets clicked switch to host state
            if(hostButton.checkClick(mx,my) == true && state == 0){
                System.out.println("Host Game button clicked");
                state = 4;
            }

            //If join game button gets clicked switch to join game
            if(joinButton.checkClick(mx,my) == true && state == 0){
                System.out.println("Join Game button clicked");
                state = 2;
                //enabling buttons for join page
                ipAdd.isAcceptingInput();
                portNum.isAcceptingInput();
            }

            if(startButton.checkClick(mx,my) == true && host == true){
                gameApi.setGameState(GameApi.SetGameStateOverworld);
            }
        }

        //if enter is inputted on Join Game try to join host at IP
        if(input.isKeyPressed(input.KEY_ENTER)){
            //If in Join State attempt to join lobby based on input
            if(state == 2){
                System.out.println("Attempting to join: " + ipAdd.getText() + " with port number: " + portNum.getText());
                try{
                    gameClient.connectToServer(ipAdd.getText(),Integer.parseInt(portNum.getText()));
                    gameApi =  new GameApi(gameClient, this);
                    connected = true;
                    state = 3;
                }catch (Exception e) {
                    e.printStackTrace();
                    failedConnect = true;
                    connected = false;
                    state = 2;
                }
            }
            //If in host state attempt to host server with given inputs
            else if(state == 4){
                System.out.println("Creating Server");
                gameClient.hostGame(Integer.parseInt(portNum.getText()), Integer.parseInt(playerNumb.getText()));
                host = true;
                state = 3;
                connected = true;
                gameApi =  new GameApi(gameClient, this);
            }

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
        }else if(state == 2){ //Join game state
            g.drawString("Enter IP address and portnumber then press enter to join game",300,83);
            g.drawString("IP ADDRESS:",200,130);
            g.drawString("PORT NUMBER:",190,200);
            ipAdd.render(gc,g);
            portNum.render(gc,g);
            if(failedConnect == true){
                g.drawString("Could not connect please try again",300,40);
            }
        }else if (state == 3){ //Lobby state
            if (host == true){
                g.drawString("GAME LOBBY: You are the host", 300,40);
                startButton.render(g);
            }else{
                g.drawString("GAME LOBBY: Waiting on host", 300,40);
            }
            if (clientList != null){
                for (int i = 0; i < clientList.length; i++) {
                    g.drawString("Client: "+clientList[i]+ " in Lobby", (20 + (250 * i)),240);
                }
            }
        }else if(state == 4) { //Host Game State
            g.drawString("Enter Port Number and Player Count then press enter to host", 300, 83);
            g.drawString("PLAYER COUNT:", 180, 330);
            g.drawString("PORT NUMBER:", 190, 200);
            playerNumb.render(gc, g);
            portNum.render(gc, g);
        }

    }

    @Override
    public int getID() {
        return GameClient.STARTUP_STATE;
    }

    public void onAlterGameState(IGameState gameState) { }

    public void onAlterPlayerState(Player player) {
        System.out.println(player.toJson().toString());
    }

    public void onCreateEntity(IEntity entity) {}

    public void onDeleteEntity(int entityId) {}

    public void onMessage(int senderId, String message) { }

    public void onSetStateToBattle() {}

    public void onSetStateToOverworld() {
        gameClient.enterState(GameClient.OVERWORLD_STATE);
    }

    public void onEndTurn() {}

    public void onLobbyClientListUpdate(String[] clientNames) {
        clientList = clientNames;
    }

    public void onLobbyIsFull() {
        gameClient.players = new Player[clientList.length];

        for (int i = 0; i < gameClient.players.length; i++) {
            gameClient.players[i] = new Player();
        }

        gameApi.updatePlayerState(gameClient.players[0]);
    }
}
