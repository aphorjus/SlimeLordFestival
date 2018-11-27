package game.client;

import game.entities.slimelord.SlimeLord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Player {
    public int id;
    public int gold;
    public List<SlimeLord> slimeLords = new LinkedList<>();

    public Player() {}

    public Player(JSONObject json) {
        slimeLords = new LinkedList<>();
        id = json.getInt("id");
        gold = json.getInt("gold");

        if (json.has("slimeLords")) {
            int count = json.getJSONArray("slimeLords").length();

            for (int i = 0; i < count; i++) {
                slimeLords.add(new SlimeLord(json.getJSONArray("slimeLords").getJSONObject(i)));
            }
        }
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("gold", gold);
        json.put("id", id);

        if (slimeLords.size() > 0) {
            JSONArray lords = new JSONArray();

            for (SlimeLord lord: slimeLords) {
                lords.put(lord.toJson());
            }

            json.put("slimeLords", lords);
        }

        return json;
    }
}
