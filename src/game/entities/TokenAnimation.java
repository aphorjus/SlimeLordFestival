package game.entities;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class TokenAnimation extends Entity {
    public TokenAnimation(Vector pos) {
        super(pos.getX(), pos.getY());

        Image i = ResourceManager.getImage("game/client/resource/token_anim.png");
        i.setFilter(Image.FILTER_NEAREST);
        SpriteSheet s = new SpriteSheet(i, 18, 20);
        Animation a = new Animation(s, 150);
        addAnimation(a);
    }
}
