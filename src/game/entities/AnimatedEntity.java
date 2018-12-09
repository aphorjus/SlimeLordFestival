package game.entities;

import jig.Entity;
import jig.Vector;
import org.newdawn.slick.Animation;

import java.util.HashMap;

public abstract class AnimatedEntity extends Entity {
    public HashMap<String, Animation> animations = new HashMap<>();
    public HashMap<String, Vector> offsets = new HashMap<>();
    public String currentAnimation;

    public void putAnimation(String name, Animation animation) {
        putAnimation(name, animation, new Vector(0, 0));
    }

    public void putAnimation(String name, Animation animation, Vector offset) {
        animations.put(name, animation);
        offsets.put(name, offset);
    }

    public void playAnimation(String animation) {
        if (!offsets.containsKey(animation)) return;

        playAnimation(animation, offsets.get(animation));
    }

    public void playAnimation(String animation, Vector offset) {
        if (!animations.containsKey(animation)
                || animation.equals(currentAnimation)) return;

        removeAnimation(animations.get(currentAnimation));
        addAnimation(animations.get(animation), offset);

        currentAnimation = animation;
    }
}
