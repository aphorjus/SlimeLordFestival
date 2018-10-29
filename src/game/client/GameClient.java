package game.client;

import game.Game;
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
    String name;
    int width;
    int height;

    Socket serverSocket;
    DataInputStream input;
    DataOutputStream output;

    public GameClient(String name, int width, int height) {
        super(name);

        this.name = name;
        this.width = width;
        this.height = height;

        loadResources();
        connect(8080);

        exit();
    }

    void loadResources() {}

    void connect(int port) {
        try {
            InetAddress ip = InetAddress.getByName("localhost");
            serverSocket = new Socket(ip, PORT_NUMBER);
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
    public void initStatesList(GameContainer gc) {}

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
