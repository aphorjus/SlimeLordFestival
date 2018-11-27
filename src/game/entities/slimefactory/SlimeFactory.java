package game.entities.slimefactory;

import game.entities.IEntity;
import game.entities.slime.Slime;
import jig.Entity;
import org.json.JSONObject;

public class SlimeFactory extends Entity implements IEntity {
    String entityType = "Factory";
    int clientID;
    int id;
    int hp;
    Slime slime;

    public SlimeFactory(JSONObject data) {
        entityType = data.getString("entityType");
        clientID = data.getInt("clientId");
        id = data.getInt("id");
        hp = data.getInt("hp");
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        data.put("entityType", entityType);
        data.put("clientId", clientID);
        data.put("id", id);
        data.put("hp", hp);

        return data;
    }

    public String getEntityType() {
        return entityType;
    }


}
