package game.client;

import org.lwjgl.Sys;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import game.InputManager;
import game.client.GameClient;
import jig.ResourceManager;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Button {
     Rectangle buttonShape = null;
     Image currentImage = null;
     Sound buttonClick = null;
    int currentX = 0;
    int currentY = 0;

    public Button(int x, int y, Image image) {
        buttonShape = new Rectangle(x, y, image.getWidth(), image.getHeight());
        currentX = x;
        currentY = y;
        currentImage = image;
        try{
            buttonClick = new Sound("game/client/resource/Click.wav");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setCurrentImage(Image newImage){
        currentImage = newImage;
    }
    //where x and y define the location of the mouse cursor
    public boolean checkClick(int x, int y) {
        Rectangle mouseShape = new Rectangle(x,y,1,1);
        if(buttonShape.intersects(mouseShape)){
            buttonClick.play();
            return true;
        }else{
            return false;
        }

    }

    public void render(Graphics g) {
        g.drawImage(currentImage, currentX, currentY);
    }
}
