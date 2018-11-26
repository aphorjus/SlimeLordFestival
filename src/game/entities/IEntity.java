package game.entities;

import org.json.JSONObject;

public interface IEntity {
    String getEntityType();
    JSONObject toJson();
}
