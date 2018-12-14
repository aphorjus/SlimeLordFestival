package game.Battles;

import game.client.GameClient;
import game.entities.AnimatedEntity;
import game.utility.AnimationSwitchTimer;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Game;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

public class AnimatedBattleBackground extends AnimatedEntity {
    String BACKGROUND_RSC = "game/client/resource/battle-background.png";
    boolean isCheering;

    public AnimatedBattleBackground() {
        setPosition(GameClient.ScreenWidth / 2, GameClient.ScreenHeight / 2);

        Image image = ResourceManager.getImage(BACKGROUND_RSC);
        image.setFilter(Image.FILTER_NEAREST);
        SpriteSheet sheet = new SpriteSheet(image, 1000, 500);
        Animation idle = new Animation(sheet, 999999);
        Animation cheer = new Animation(sheet, 200);
        putAnimation("idle", idle);
        putAnimation("cheer", cheer);

        playAnimation("idle");
    }

    public void cheer() {
        if (currentAnimation == "cheer") return;

        playAnimation("cheer");

        AnimationSwitchTimer a = new AnimationSwitchTimer(this, "idle", 1000);
        a.start();
    }
}
