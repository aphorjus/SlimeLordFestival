package game.client;

import game.GameApi;
import game.GameApiRequest;
import game.InputManager;
import game.client.states.PlayingState;
import game.client.states.StartUpState;
import game.entities.slimelord.SlimeLord;
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
    String HOST_NAME = "localhost";
    String name;
    int width;
    int height;
    int tokens;
    LinkedList<SlimeLord> slimeLords;

    public InputManager inputManager;
    public Socket serverSocket;
    public DataInputStream input;
    public DataOutputStream output;

    public static final int STARTUP_STATE = 0;
    public static final int PLAYING_STATE = 1;

    public static int ScreenWidth;
    public static int ScreenHeight;
    public static int ImageWidth = 1392;
    public static int ImageHeight = 800;

    private Board board;

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

        loadResources();
        connectToServer(HOST_NAME, PORT_NUMBER);
    }

    void loadResources() {}

    void connectToServer(String hostName, int port) {
        try {
            InetAddress ip = InetAddress.getByName(hostName);
            serverSocket = new Socket(ip, port);
            input = new DataInputStream(serverSocket.getInputStream());
            output = new DataOutputStream(serverSocket.getOutputStream());
            sendConnectionConfirmation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void exit() {
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

    public void setGameState(String newState) {
        if (!newState.equals(GameApi.SetGameStateBattle) || !newState.equals(GameApi.SetGameStateOverworld)) return;
        sendRequest(new GameApiRequest(newState));
    }

    public void sendMessage(String message) {
        if (message == null) return;

        JSONObject body = new JSONObject();
        body.put("text", message);
        sendRequest(new GameApiRequest(GameApi.Message, body));
    }

    void sendRequest(GameApiRequest req) {
        try {
            output.writeUTF(req.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        addState(new StartUpState());
        addState(new PlayingState());
    }

    public static void main(String[] args) {
        AppGameContainer app;
        int width = 1000;
        int height = 500;

        try {
            app = new AppGameContainer(new GameClient("SlimeLordFestival", width, height));
            app.setDisplayMode(width, height, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
