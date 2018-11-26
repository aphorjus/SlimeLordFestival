package game.client;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import game.InputManager;
import game.client.GameClient;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Button {
     Rectangle buttonShape = null;
     Image currentImage = null;
    int currentX = 0;
    int currentY = 0;

    public Button(int x, int y, Image image) {
        buttonShape = new Rectangle(x, y, image.getWidth(), image.getHeight());
        currentX = x;
        currentY = y;
        currentImage = image;
    }

    //where x and y define the location of the mouse cursor
    public boolean checkClick(int x, int y) {
        Rectangle mouseShape = new Rectangle(x,y,1,1);
        return buttonShape.intersects(mouseShape);
    }

    public void render(Graphics g) {
        g.drawImage(currentImage, currentX, currentY);
    }
}
