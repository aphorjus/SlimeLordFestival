package game;

import game.entities.IEntity;
import org.json.JSONObject;

public class IntVector implements IEntity {

    String entityType = "IntVector";
    public int x;
    public int y;

    public IntVector(int x, int y){
        this.x = x;
        this.y = y;
    }
    public IntVector(JSONObject data){
        data.getInt("x");
        data.getInt("y");
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public JSONObject toJson() {
        JSONObject data = new JSONObject();

        data.put("x", x);
        data.put("y", y);

        return data;
    }
}
