package game.entities;

import jig.Entity;
import org.newdawn.slick.Animation;

import java.util.HashMap;

public abstract class AnimatedEntity extends Entity {
    public HashMap<String, Animation> animations = new HashMap<>();
    public String currentAnimation;

    public void putAnimation(String name, Animation animation) {
        animations.put(name, animation);
    }

    public void playAnimation(String animation) {
        if (!animations.containsKey(animation) || animation.equals(currentAnimation)) return;

        removeAnimation(animations.get(currentAnimation));
        addAnimation(animations.get(animation));

        currentAnimation = animation;
    }
}
