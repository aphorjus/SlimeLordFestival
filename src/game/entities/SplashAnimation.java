package game.entities;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class SplashAnimation extends Entity {
    String GREEN_RSC = "game/client/resource/green-splash.png";
    String BLUE_RSC = "game/client/resource/blue-splash.png";
    String YELLOW_RSC = "game/client/resource/yellow-splash.png";
    String RED_RSC = "game/client/resource/red-splash.png";

    public SplashAnimation(Vector pos, String color) {
        super(pos.getX(), pos.getY());

        String splashColor = BLUE_RSC;
        switch (color) {
            case "blue":
                splashColor = BLUE_RSC;
                break;
            case "green":
                splashColor = GREEN_RSC;
                break;
            case "yellow":
                splashColor = YELLOW_RSC;
                break;
            case "red":
                splashColor = RED_RSC;
                break;
        }

        Image splashImage = ResourceManager.getImage(splashColor);
        splashImage.setFilter(Image.FILTER_NEAREST);
        SpriteSheet splashSheet = new SpriteSheet(splashImage, 16, 16);
        Animation a = new Animation(splashSheet, 150);
        addAnimation(a);
    }

    public SplashAnimation(Vector pos, int clientID) {
        super(pos.getX(), pos.getY());

        String splashColor = BLUE_RSC;
        switch (clientID) {
            case 0:
                splashColor = BLUE_RSC;
                break;
            case 1:
                splashColor = GREEN_RSC;
                break;
            case 2:
                splashColor = YELLOW_RSC;
                break;
            case 3:
                splashColor = RED_RSC;
                break;
        }

        Image splashImage = ResourceManager.getImage(splashColor);
        splashImage.setFilter(Image.FILTER_NEAREST);
        SpriteSheet splashSheet = new SpriteSheet(splashImage, 16, 16);
        Animation a = new Animation(splashSheet, 100);
        addAnimation(a);
    }
}
