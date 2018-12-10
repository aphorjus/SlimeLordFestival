package game.client.states;

import game.IGameState;
import game.InputManager;
import game.api.GameApi;
import game.api.GameApiListener;
import game.client.GameClient;
import game.client.Player;
import game.entities.IEntity;
import game.entities.slimelord.SlimeLord;
import game.server.GameServer;
import jig.ResourceManager;
import org.lwjgl.Sys;
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
    public static final String LOBBYBOARD = "game/client/resource/LobbyBoard.png";
    public static final String LOBBYBACKGROUND = "game/client/resource/LobbyBackground.png";
    public static final String LOBBYBACKGROUND2 = "game/client/resource/LobbyBackground.png";
    public static final String LOBBYTITLE = "game/client/resource/LobbyTitle.png";
    public static final String HOSTTITLE = "game/client/resource/ServerInfo.png";
    public static final String START = "game/client/resource/Start.png";

    Button joinButton = null;
    Button hostButton = null;
    Button startButton = null;
    GameServer currentGameServer = null;
    GameClient gameClient;
    GameApi gameApi;
    TextField ipAdd;
    TextField portNum;
    TextField playerNumb;
    boolean failedConnect;
    boolean connected;
    boolean host;
    boolean isLobbyFull;
    String[] clientList;
    double backgroundX;
    double backgroundOneX = 0;
    double backgroundTwoX = -1000;
    int state = 0; // 0 is title screen, 1 is HostGame screen, 2 is JoinGame screen, 3 is join lobby

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        GameClient game = (GameClient) sbg;
        gameClient = (GameClient) sbg;
        inputManager = game.inputManager;
        ResourceManager.loadImage(TITLE);
        ResourceManager.loadImage(HOSTGAME);
        ResourceManager.loadImage(JOINGAME);
        ResourceManager.loadImage(START);
        ResourceManager.loadImage(STARTGAME);
        ResourceManager.loadImage(LOBBYBACKGROUND);
        ResourceManager.loadImage(LOBBYBACKGROUND2);
        ResourceManager.loadImage(LOBBYBOARD);
        ResourceManager.loadImage(HOSTTITLE);
        ResourceManager.loadImage(LOBBYTITLE);
        Image joinGame = new Image(JOINGAME);
        Image hostGame = new Image(HOSTGAME);
        Image startGame = new Image(START);
        joinButton = new Button(60, 440, joinGame);
        hostButton = new Button(640, 440, hostGame);
        startButton = new Button(604, 358, startGame);
        gameApi = new GameApi((GameClient) sbg, this);
        failedConnect = false;
        connected = false;
        host = false;
        backgroundX = 0;
        //Creating text field for IP address
        ipAdd = new TextField(gc, gc.getDefaultFont(), 417, 252, 150, 20);
        ipAdd.setBackgroundColor(Color.black);
        ipAdd.setBorderColor(Color.white);
        ipAdd.setTextColor(Color.white);
        ipAdd.setMaxLength(253);
        ipAdd.setFocus(true);
        ipAdd.isAcceptingInput();

        //Creating text field for port number
        portNum = new TextField(gc, gc.getDefaultFont(), 417, 190, 150, 20);
        portNum.setBackgroundColor(Color.black);
        portNum.setBorderColor(Color.white);
        portNum.setTextColor(Color.white);
        portNum.setMaxLength(253);
        portNum.setFocus(true);
        portNum.isAcceptingInput();
    }

    @Override
    public void enter(GameContainer gc, StateBasedGame sbg) {
        gameApi = new GameApi((GameClient)sbg, this);

    }

    public void backgoundMotion(Graphics g){
        //Parallex lobby background
        backgroundOneX =backgroundOneX+.009;
        backgroundTwoX=backgroundTwoX+.009;
        if(backgroundOneX>=999) {
            backgroundOneX = -1000;
        }else if(backgroundTwoX >=999){
            backgroundTwoX = -1000;
        }
        g.drawImage(ResourceManager.getImage(LOBBYBACKGROUND),(float)backgroundOneX,0);
        g.drawImage(ResourceManager.getImage(LOBBYBACKGROUND),(float)backgroundTwoX,0);
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
                //Empty ipadd input, so it can be used for player count
                portNum.setText("");
                ipAdd.setText("");
            }

            //If join game button gets clicked switch to join game
            if(joinButton.checkClick(mx,my) == true && state == 0){
                System.out.println("Join Game button clicked");
                state = 2;
                //enabling buttons for join page
                portNum.setText("");
                ipAdd.setText("");
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
                currentGameServer = gameClient.hostGame(Integer.parseInt(portNum.getText()), Integer.parseInt(ipAdd.getText()));
                host = true;
                state = 3;
                connected = true;
                gameApi =  new GameApi(gameClient, this);
            }

        }

        if(input.isKeyPressed(Input.KEY_ESCAPE)){
            //resetting all variables
            state = 0;
            failedConnect = false;
            connected = false;
            isLobbyFull = false;

            //gameClient.exit();
            //if you are hosting a game stop it
            if(currentGameServer!=null){
                currentGameServer.end();
                currentGameServer = null;
            }
        }

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;

        if (state == 0){ //MAIN MENU
            g.drawImage(ResourceManager.getImage(TITLE),
                    0, 0);
            joinButton.render(g);
            hostButton.render(g);
        }else if(state == 1){ //DEPRECATED Host state
            g.drawString("You are Hosting a game at 192.181.1.3",300,83);
        }else if(state == 2){ //JOIN GAME MENU
            backgoundMotion(g);
            g.drawImage(ResourceManager.getImage(LOBBYBOARD), 0, 0);
            g.drawImage(ResourceManager.getImage(JOINGAME),355,107);
            g.drawString("(press \"ENTER\" to join)", 392,374);
            g.drawString("IP Address", 417, 230);
            g.drawString("Port Number", 417, 170);
            ipAdd.render(gc,g);
            portNum.render(gc,g);
            if(failedConnect == true){
                g.setColor(Color.red);
                g.drawString("Could not connect please try again",334,354);
                g.setColor(Color.white);
            }
        }else if (state == 3){ //LOBBY
            backgoundMotion(g);
            g.drawImage(ResourceManager.getImage(LOBBYBOARD), 0, 0);
            g.drawImage(ResourceManager.getImage(LOBBYTITLE),355,107);
            if (host == true){
                //g.drawString("GAME LOBBY: You are the host", 300,40);
                if(isLobbyFull == true){
                    startButton.render(g);
                }
            }else{
                //g.drawString("GAME LOBBY: Waiting on host", 300,40);
            }
            if (clientList != null){
                for (int i = 0; i < clientList.length; i++) {
                    g.drawString("Player "+(Integer.parseInt(clientList[i]) + 1) + " in Lobby", 253,(158 + (i*40)));
                }
            }
        }else if(state == 4) { //HOST GAME MENU
            backgoundMotion(g);
            g.drawImage(ResourceManager.getImage(LOBBYBOARD), 0, 0);
            g.drawImage(ResourceManager.getImage(HOSTGAME),355,107);
            g.drawString("(press \"ENTER\" to host)", 392,374);
            //g.drawString("Enter Port Number and Player Count then press enter to host", 233, 114);
            g.drawString("Player Count", 417, 230);
            g.drawString("Port Number", 417, 170);
            ipAdd.render(gc, g);
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

    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {}

    public void onSetStateToOverworld() {
        gameClient.enterState(GameClient.OVERWORLD_STATE);
    }

    public void onEndTurn() {}

    public void onLobbyClientListUpdate(String[] clientNames) {
        clientList = clientNames;
    }

    public void onLobbyIsFull() {
        gameClient.players = new Player[clientList.length];
        isLobbyFull = true;
        for (int i = 0; i < gameClient.players.length; i++) {
            gameClient.players[i] = new Player();
        }

        gameApi.updatePlayerState(gameClient.players[0]);
    }

    public void onConnectionConfirmation(int myId) {
        if (gameClient.myId == -1) {
            gameClient.myId = myId;
            System.out.println("MyId is: " + myId);
        }
    }
}
