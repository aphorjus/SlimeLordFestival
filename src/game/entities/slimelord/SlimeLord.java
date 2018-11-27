package game.entities.slimelord;

import game.entities.IEntity;
import game.entities.slimefactory.SlimeFactory;
import jig.Entity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

public class SlimeLord extends Entity implements IEntity {
    String entityType = "slime_lord";
    int id;
    String name;
    int totalMovement;
    int remainingMovement;
    LinkedList<SlimeLordAbility> abilities;
    LinkedList<SlimeFactory> factories;

    public SlimeLord(JSONObject data) {
        abilities = new LinkedList<>();
        factories = new LinkedList<>();

        entityType = data.getString("entityType");
        id = data.getInt("id");
        name = data.getString("name");
        totalMovement = data.getInt("totalMovement");
        remainingMovement = data.getInt("remainingMovement");

        if (data.has("abilities")) {
            JSONArray jsonAbilities = data.getJSONArray("abilities");

            for (int i = 0; i < jsonAbilities.length(); i++) {
                abilities.add(new SlimeLordAbility(jsonAbilities.getJSONObject(i)));
            }
        }

        if (data.has("factories")) {
            JSONArray jsonFactories = data.getJSONArray("factories");

            for (int i = 0; i < jsonFactories.length(); i++) {
                factories.add(new SlimeFactory(jsonFactories.getJSONObject(i)));
            }
        }
    }

    public String getEntityType() {
        return entityType;
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("entityType", entityType);
        data.put("id", id);
        data.put("name", name);
        data.put("totalMovement", totalMovement);
        data.put("remainingMovement", remainingMovement);

        if (abilities.size() > 0) {
            JSONArray jsonAbilities = new JSONArray();

            for (SlimeLordAbility ability : abilities) {
                jsonAbilities.put(ability.toJson());
            }

            data.put("abilities", jsonAbilities);
        }

        if (factories.size() > 0) {
            JSONArray jsonFactories = new JSONArray();

            for (SlimeFactory factory : factories) {
                jsonFactories.put(factory.toJson());
            }

            data.put("factories", factories);
        }

        return data;
    }
}
