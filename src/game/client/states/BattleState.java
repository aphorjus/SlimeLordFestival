package game.client.states;

import game.Battles.BattleGridTile;
import game.entities.slime.Slime;
import jig.Vector;
import game.Battles.BattleGrid;
import game.InputManager;
import game.client.GameClient;
import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class BattleState extends BasicGameState {
    InputManager inputManager;
    BattleGrid battleGrid;

    public static int[][] PLAIN_MAP =
            {{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,2,1,1,1,1,1,1,2,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
             {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}};


    @Override
    public void init(GameContainer gc, StateBasedGame sbg) {
        GameClient game = (GameClient)sbg;
        inputManager = game.inputManager;



        this.battleGrid = new BattleGrid(game.ScreenHeight, game.ScreenWidth,
                30, 200, BattleState.PLAIN_MAP);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delta) {
        Input input = gc.getInput();

        if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
            this.battleGrid.selectTile(mousePosition);

        }
        if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)){
//            Vector mousePosition = new Vector(input.getMouseX(), input.getMouseY());
//            this.battleGrid.selectTile(mousePosition);
            this.battleGrid.deselectTile();
        }
    }

    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        GameClient bg = (GameClient)sbg;
        battleGrid.render(g);
    }

    @Override
    public int getID() {
        return GameClient.BATTLE_STATE;
    }

}
