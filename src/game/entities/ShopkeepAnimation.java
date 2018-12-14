package game.entities;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class ShopkeepAnimation extends Entity {
    public ShopkeepAnimation(Vector pos) {
        super(pos.getX(), pos.getY());

        Image i = ResourceManager.getImage("game/client/resource/shopSS.png");
        i.setFilter(Image.FILTER_NEAREST);
        SpriteSheet s = new SpriteSheet(i, 137, 110);
        Animation a = new Animation(s, 200);
        addAnimation(a);
    }
}
