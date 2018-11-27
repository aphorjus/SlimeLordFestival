package game.api;

import org.json.JSONObject;

public class GameApiRequest {
    public String type;
    public JSONObject body;

    public GameApiRequest(String actionType) {
        this.type = actionType;
        this.body = new JSONObject();
    }

    public GameApiRequest(String actionType, JSONObject body) {
        this.type = actionType;
        this.body = body;
    }

    public GameApiRequest(JSONObject json) {
        type = json.getString("type");

        if (json.has("body")) body = json.getJSONObject("body");
        else body = new JSONObject();
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("type", this.type);

        if (this.body != null) json.put("body", body);

        return json;
    }

    public String toString() {
        return toJson().toString();
    }
}
