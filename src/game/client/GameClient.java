package game.client;

import game.Game;
import game.client.states.PlayingState;
import game.client.states.StartUpState;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient extends StateBasedGame {
    int PORT_NUMBER = 8080;
    String HOST_NAME = "localhost";
    String name;
    int width;
    int height;

    Socket serverSocket;
    DataInputStream input;
    DataOutputStream output;

    public static final int STARTUP_STATE = 0;
    public static final int PLAYING_STATE = 1;

    public GameClient(String name, int width, int height) {
        super(name);

        this.name = name;
        this.width = width;
        this.height = height;

        loadResources();
        connectToServer(HOST_NAME, PORT_NUMBER);

        exit();
    }

    void loadResources() {}

    void connectToServer(String hostName, int port) {
        try {
            InetAddress ip = InetAddress.getByName(hostName);
            serverSocket = new Socket(ip, port);
            input = new DataInputStream(serverSocket.getInputStream());
            output = new DataOutputStream(serverSocket.getOutputStream());
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

    @Override
    public void initStatesList(GameContainer gc) {
        addState(new StartUpState());
        addState(new PlayingState());
    }

    public static void main(String[] args) {
        AppGameContainer app;
        int width = 320;
        int height = 176;
        int displayWidth = 2560;
        int displayHeight = 1600;

        try {
            app = new AppGameContainer(new ScalableGame(new GameClient("SlimeLordFestival", width, height), width, height));
            app.setDisplayMode(displayWidth, displayHeight, true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
