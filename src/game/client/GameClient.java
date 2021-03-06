package game.client;

import game.api.GameApi;
import game.api.GameApiRequest;
import game.InputManager;
import game.client.states.BattleState;
import game.client.states.OverworldState;
import game.client.states.StartUpState;
import game.entities.slimelord.SlimeLord;
import game.server.GameServer;
import org.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;

import jig.Entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

public class GameClient extends StateBasedGame {
    int PORT_NUMBER = 8080;
    String HOST_NAME = "127.0.0.1";
    String name;
    int width;
    int height;
    int tokens = 0;
    LinkedList<SlimeLord> slimeLords;
    public Player[] players;

    public InputManager inputManager;
    public Socket serverSocket;
    public DataInputStream input;
    public DataOutputStream output;
    GameServer runningServer;

    public static final int STARTUP_STATE = 0;
    public static final int OVERWORLD_STATE = 1;
    public static final int BATTLE_STATE = 2;

    public static int ScreenWidth;
    public static int ScreenHeight;
    public static int ImageWidth = 1392;
    public static int ImageHeight = 800;

    public int battleStateWinner = -1;
    public int battleStateLoser = -1;

    public int myId = -1;

    private Board board;
    private BattleState battleState;

    // For the Board class, which contains the overworld map
    public Board getBoard() {
        return board;
    }

    public GameClient(String name, int width, int height) {
        super(name);

        this.name = name;
        this.width = width;
        this.height = height;

        ScreenHeight = height;
        ScreenWidth = width;

        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        board = new Board();
    }

    public void connectToServer(String hostName, int port) throws Exception {
        InetAddress ip = InetAddress.getByName(hostName);
        serverSocket = new Socket(ip, port);
        input = new DataInputStream(serverSocket.getInputStream());
        output = new DataOutputStream(serverSocket.getOutputStream());
        sendConnectionConfirmation();
    }

    public void setBattleStateWinner(int id) {
        battleStateWinner = id;
    }

    public void setBattleStateLoser(int id){
        battleStateLoser = id;
    }

    public int getTokens(){
        return this.tokens;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void exit() {
        try {
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendConnectionConfirmation() {
        sendRequest(new GameApiRequest(GameApi.ConnectionConfirmation));
    }

    void sendRequest(GameApiRequest req) {
        try {
            output.writeUTF(req.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameServer hostGame(int portNumber, int playerCount) {
        try {
            runningServer = new GameServer(portNumber, playerCount);
            runningServer.start();
            connectToServer("localhost", portNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return runningServer;
    }

    @Override
    public void initStatesList(GameContainer gc) {
        board.initialize();
        int[] keys = {
                Input.KEY_W,
                Input.KEY_S,
                Input.KEY_A,
                Input.KEY_D,
                Input.KEY_ENTER
        };

        inputManager = new InputManager(gc, keys);
        battleState = new BattleState();
        addState(new StartUpState());
        addState(new OverworldState());
        addState(battleState);
    }

    public void startBattle(SlimeLord one, SlimeLord two) {
        battleState.onSetStateToBattle(one, two);
        enterState(battleState.getID());
    }

    public static void main(String[] args) {
        AppGameContainer app;
        int width = 1000;
        int height = 500;

        try {
            app = new AppGameContainer(new GameClient("SlimeLordFestival", width, height));
            app.setDisplayMode(width, height, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
