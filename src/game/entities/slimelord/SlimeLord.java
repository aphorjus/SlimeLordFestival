package game.entities.slimelord;

import game.client.Board;
import game.entities.IEntity;
import game.entities.slimefactory.SlimeFactory;
import jig.Entity;
import jig.ResourceManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;

import java.util.LinkedList;
import java.util.UUID;

public class SlimeLord extends Entity implements IEntity {
    String entityType = "slime_lord";
    public int clientID;
    public String id;
    String name;
    int totalMovement;
    int remainingMovement;
    LinkedList<SlimeLordAbility> abilities;
    public LinkedList<SlimeFactory> factories;

    private float xoffset = 0;
    private float yoffset = 0;
    private float xpos;
    private float ypos;

    public SlimeLord(int clientID){
        this.clientID = clientID;
        this.id = UUID.randomUUID().toString();
        this.totalMovement = 10;
        this.remainingMovement = totalMovement;
        this.abilities = new LinkedList<SlimeLordAbility>();

        this.factories = new LinkedList<SlimeFactory>();
        this.factories.add(new SlimeFactory(this.clientID));
        this.factories.add(new SlimeFactory(this.clientID));

        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME1_RSC));
    }

    public void render(Graphics g){
        float x = xpos - xoffset;
        float y = ypos - yoffset;
        g.drawImage(ResourceManager.getImage(Board.SLIME1_RSC), x+1, y+1);
    }

    public void setOffsets(float xoffset, float yoffset) {
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    public void setPosition(float x, float y) {
        this.xpos = x;
        this.ypos = y;
    }


    public SlimeLord(JSONObject data) {
        abilities = new LinkedList<>();
        factories = new LinkedList<>();

        entityType = data.getString("entityType");
        clientID = data.getInt("clientID");
        id = data.getString("id");
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
        data.put("clientID", clientID);
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
