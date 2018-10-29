package game.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameClient extends StateBasedGame {
    String name;
    int width;
    int height;

    public GameClient(String name, int width, int height) {
        super(name);

        this.name = name;
        this.width = width;
        this.height = height;

        loadResources();
    }

    void loadResources() {}

    @Override
    public void initStatesList(GameContainer gc) {}

    public static void main(String[] args) {
        AppGameContainer app;
        int width = 320;
        int height = 176;
        int displayWidth = 2560;
        int displayHeight = 1600;

        try {
            app = new AppGameContainer(new ScalableGame(new GameClient("GarlicBoy", width, height), width, height));
            app.setDisplayMode(displayWidth, displayHeight, true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
