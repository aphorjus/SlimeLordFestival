package game.client;

import game.Game;
import game.InputManager;
import game.client.states.PlayingState;
import game.client.states.StartUpState;
import game.entities.slimelord.SlimeLord;
import org.json.JSONObject;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;

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

    public GameClient(String name, int width, int height) {
        super(name);

        this.name = name;
        this.width = width;
        this.height = height;

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
        try {
            JSONObject confirmation = new JSONObject();
            confirmation.put("type", "CONFIRMATION");
            output.writeUTF(confirmation.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        JSONObject body = new JSONObject();
        body.put("text", message);
        Post(Game.Action.MESSAGE, body);
    }

    private void Post(String type, JSONObject body) {
        JSONObject post = new JSONObject();
        post.put("type", Game.Api.POST);
        post.put("actionType", type);
        post.put("body", body.toString());

        try {
            output.writeUTF(post.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Get() {}

    private void Delete() {}

    @Override
    public void initStatesList(GameContainer gc) {
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
        int width = 320;
        int height = 176;

        try {
            app = new AppGameContainer(new GameClient("SlimeLordFestival", width, height));
            app.setDisplayMode(width, height, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
