package game.client.states;

import game.InputManager;
import game.client.GameClient;
import jig.ResourceManager;
import org.newdawn.slick.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import game.client.Board;
import org.newdawn.slick.Image;
import game.client.Button;

public class StartUpState extends BasicGameState {
    InputManager inputManager;
    public static final String TITLE = "game/client/resource/title.png";
    public static final String JOINGAME = "game/client/resource/JoinGame.png";
    public static final String HOSTGAME = "game/client/resource/HostGame.png";
    Button joinButton = null;
    Button hostButton = null;

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        GameClient game = (GameClient)sbg;
        inputManager = game.inputManager;
        ResourceManager.loadImage(TITLE);
        ResourceManager.loadImage(HOSTGAME);
        ResourceManager.loadImage(JOINGAME);
        Image joinGame = new Image(JOINGAME);
        Image hostGame = new Image(HOSTGAME);
        joinButton = new Button(60,440,joinGame);
        hostButton = new Button(640,440,hostGame);
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
            if(hostButton.checkClick(mx,yx) == true){
                System.out.println("Host Game button clicked");
                bg.enterState(bg.PLAYING_STATE);
            }
            if(joinButton.checkClick(mx,yx) == true){
                System.out.println("Join Game button clicked");
                bg.enterState(bg.PLAYING_STATE);
            }
        }

    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        g.drawImage(ResourceManager.getImage(TITLE),
                0, 0);
        //g.drawImage(ResourceManager.getImage(JOINGAME),
        //        60, 440);
        //g.drawImage(ResourceManager.getImage(HOSTGAME),
        //       640, 440);
        joinButton.render(g);
        hostButton.render(g);

    }

    @Override
    public int getID() {
        return GameClient.STARTUP_STATE;
    }
}
