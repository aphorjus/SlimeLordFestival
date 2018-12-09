package game.utility;

import game.entities.AnimatedEntity;

public class AnimationSwitchTimer extends Thread {
    int timeLimit;
    String nextAnimation;
    AnimatedEntity entity;

    public AnimationSwitchTimer(AnimatedEntity entity, String nextAnimation, int milliseconds) {
        this.entity = entity;
        this.timeLimit = milliseconds;
        this.nextAnimation = nextAnimation;
    }

    @Override
    public void run() {
        try {
            sleep(this.timeLimit);
            entity.playAnimation(nextAnimation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
