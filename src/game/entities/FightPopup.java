package game.entities;

import game.api.GameApi;
import game.client.states.OverworldState;
import game.entities.slime.Slime;
import game.entities.slimelord.SlimeLord;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Input;

public class FightPopup extends Entity {
    String FIGHT_POPUP = "game/client/resource/fight-popup.png";

    public SlimeLord one;
    public SlimeLord two;

    public boolean IsFighting;
    public boolean IsCoward;

    public FightPopup(Vector pos, SlimeLord one, SlimeLord two) {
        super(pos.getX(), pos.getY());

        this.one = one;
        this.two = two;
        addImage(ResourceManager.getImage(FIGHT_POPUP));
    }

    public void update(Input input)
        {
            if (input.isKeyPressed(Input.KEY_Y)) {
                IsFighting = true;
            } else if (input.isKeyPressed(Input.KEY_N)) {
                IsCoward = true;
            }
        }
}
