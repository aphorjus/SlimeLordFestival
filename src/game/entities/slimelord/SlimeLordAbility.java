package game.entities.slimelord;

import game.entities.IEntity;
import org.json.JSONObject;

public class SlimeLordAbility implements IEntity {

    public int type;

    public SlimeLordAbility(){ }

    public SlimeLordAbility(JSONObject data) {}

    public String getEntityType() {
        return "slime_lord_ability";
    }

    public JSONObject toJson() {
        return new JSONObject();
    }
}
