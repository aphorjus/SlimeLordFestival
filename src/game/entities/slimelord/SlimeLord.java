package game.entities.slimelord;

import game.IGameState;
import game.api.GameApiListener;
import game.client.Board;
import game.client.Player;
import game.client.Turn;
import game.entities.AnimatedEntity;
import game.entities.IEntity;
import game.entities.slimefactory.SlimeFactory;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.util.LinkedList;
import java.util.UUID;

public class SlimeLord extends AnimatedEntity implements IEntity {
    String GREEN_SLIMELORD_IDLE = "game/client/resource/slime-lord-green.png";
    String BLUE_SLIMELORD_IDLE = "game/client/resource/slime-lord-blue.png";
    String YELLOW_SLIMELORD_IDLE = "game/client/resource/slime-lord-yellow.png";
    String RED_SLIMELORD_IDLE = "game/client/resource/slime-lord-red.png";


    String entityType = "slime_lord";
    public int clientID;
    public String id;
    public Vector tilePosition = new Vector(0, 0);
    String name = "";
    public boolean hasMoved;
    public int totalMovement = 15;
    public int remainingMovement = 15;
    LinkedList<BattleAbility> battleAbilities;
    public LinkedList<String> abilities;
    public LinkedList<SlimeFactory> factories;
    public LinkedList<String> specialSlimes;


    public Vector cameraOffset = new Vector(0, 0);
    public float xpos;
    public float ypos;
    private Turn turn;
    String color = "blue";

    public SlimeLord(int clientID){
        this.clientID = clientID;

        switch (clientID) {
            case 0:
                this.color = "blue";
                break;
            case 1:
                this.color = "green";
                break;
            case 2:
                this.color = "red";
                break;
            case 3:
                this.color = "yellow";
                break;
        }

        this.id = UUID.randomUUID().toString();
        this.specialSlimes = new LinkedList<>();
        this.abilities = new LinkedList<>();
        this.factories = new LinkedList<>();
        this.specialSlimes = new LinkedList<>();
        this.factories.add(new SlimeFactory(this.clientID));
        this.factories.add(new SlimeFactory(this.clientID));

        this.initializeAnimations();
    }

    void initializeAnimations() {
        String imageName = GREEN_SLIMELORD_IDLE;

        switch (this.color) {
            case "blue":
                imageName = BLUE_SLIMELORD_IDLE;
                break;
            case "green":
                imageName = GREEN_SLIMELORD_IDLE;
                break;
            case "yellow":
                imageName = YELLOW_SLIMELORD_IDLE;
                break;
            case "red":
                imageName = RED_SLIMELORD_IDLE;
                break;

        }

        Image idle = ResourceManager.getImage(imageName);
        idle.setFilter(Image.FILTER_NEAREST);
        SpriteSheet idleSheet = new SpriteSheet(idle, 32, 32);
        putAnimation("idle", new Animation(idleSheet, 99999), new Vector(8, 0));
        putAnimation("victory", new Animation(idleSheet, 250), new Vector(8, 0));
        playAnimation("idle");
    }

    public void moveTo(float x, float y) {
        this.translate(x - this.getX(),y - this.getY());
    }

    public boolean makeMove() {
        return true;
    }

    public void moveRight() {
        if(makeMove()) {
            this.translate(16,0);
        }
    }

    public void moveLeft() {
        if(makeMove()) {
            this.translate(-16, 0);
        }
    }

    public void moveUp() {
        if(makeMove()) {
            this.translate(0, -16);
        }
    }

    public void moveDown() {
        if(makeMove()) {
            this.translate(0, 16);
        }
    }


    public void setCameraOffset(Vector offset) {
        cameraOffset = offset;
    }

    public void positionForCamera() {
        translate(-cameraOffset.getX(), -cameraOffset.getY());
    }

    public void positionToOrigin() {
        translate(cameraOffset.getX(), cameraOffset.getY());
    }

    public void setPosition(float x, float y) {
        this.xpos = x;
        this.ypos = y;
    }


    public SlimeLord(JSONObject data) {
        abilities = new LinkedList<>();
        factories = new LinkedList<>();
        specialSlimes = new LinkedList<>();

        entityType = data.getString("entityType");
        clientID = data.getInt("clientID");
        id = data.getString("id");
        name = data.getString("name");
        totalMovement = data.getInt("totalMovement");
        remainingMovement = data.getInt("remainingMovement");
        xpos = data.getFloat("Xposition");
        ypos = data.getFloat("Yposition");

        tilePosition = new Vector(data.getInt("tileX"), data.getInt("tileY"));
        hasMoved = data.getBoolean("hasMoved");

        if (data.has("abilities")) {
            JSONArray jsonAbilities = data.getJSONArray("abilities");

            for (int i = 0; i < jsonAbilities.length(); i++) {
                abilities.add(jsonAbilities.getString(i));
            }
        }

        if (data.has("factories")) {
            JSONArray jsonFactories = data.getJSONArray("factories");

            for (int i = 0; i < jsonFactories.length(); i++) {
                factories.add(new SlimeFactory(jsonFactories.getJSONObject(i)));
            }
        }

        if (data.has("specialSlimes")){
            JSONArray jsonSlimes = data.getJSONArray("specialSlimes");

            for (int i = 0; i < jsonSlimes.length(); i++){
                specialSlimes.add(jsonSlimes.getString(i));
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
        data.put("Xposition", this.getX());
        data.put("Yposition", this.getY());
        data.put("tileX", (int)this.tilePosition.getX());
        data.put("tileY", (int)this.tilePosition.getY());
        data.put("hasMoved", hasMoved);

        if (abilities.size() > 0) {
            JSONArray jsonAbilities = new JSONArray();

            for (String ability : abilities) {
                jsonAbilities.put(ability);
            }

            data.put("abilities", jsonAbilities);
        }

        if (factories.size() > 0) {
            JSONArray jsonFactories = new JSONArray();

            for (SlimeFactory factory : factories) {
                jsonFactories.put(factory.toJson());
            }

            data.put("factories", jsonFactories);
        }

        if (specialSlimes.size() > 0) {
            JSONArray jsonSlimes = new JSONArray();

            for (String slime : specialSlimes) {
                jsonSlimes.put(slime);cc
            }

            data.put("specialSlimes", jsonSlimes);
        }

        return data;
    }

    public void addAbility(String ability){
        abilities.add(ability);
    }

    public String getAbility(int i){
        if( abilities.size() > i ) {
            return abilities.get(i);
        }
        return "NONE";
    }
}
