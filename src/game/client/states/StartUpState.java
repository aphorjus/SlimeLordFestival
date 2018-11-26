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
    int state = 0; // 0 is title screen, 1 is HostGame screen, 2 is JoinGame screen

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
        if(input.isKeyPressed(Input.KEY_ESCAPE)){
            state = 0;
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
            g.drawString("Enter IP of game you want to join",300,83);
        }

    }

    @Override
    public int getID() {
        return GameClient.STARTUP_STATE;
    }
}
