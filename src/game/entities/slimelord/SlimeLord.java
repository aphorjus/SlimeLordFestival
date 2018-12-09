package game.entities.slimelord;

import com.sun.org.apache.regexp.internal.RE;
import game.IGameState;
import game.api.GameApiListener;
import game.client.Board;
import game.client.Player;
import game.client.Turn;
import game.entities.AnimatedEntity;
import game.entities.IEntity;
import game.entities.slimefactory.SlimeFactory;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.util.LinkedList;
import java.util.UUID;

public class SlimeLord extends AnimatedEntity implements IEntity, GameApiListener {
    String GREEN_IDLE = "game/client/resource/green-slime-idle.png";
    String BLUE_IDLE = "game/client/resource/blue-slime-idle.png";
    String YELLOW_IDLE = "game/client/resource/yellow-slime-idle.png";
    String RED_IDLE = "game/client/resource/red-slime-idle.png";

    String entityType = "slime_lord";
    public int clientID;
    public String id;
    String name;
    int totalMovement;
    int remainingMovement;
    LinkedList<SlimeLordAbility> abilities;
    public LinkedList<SlimeFactory> factories;
    public LinkedList<String> specialSlimes;

    private float xoffset = 0;
    private float yoffset = 0;
    private float xpos;
    private float ypos;
    private Turn turn;
    String color = "blue";

    public SlimeLord(int clientID){
        System.out.println("slimelord created" + clientID);
        this.clientID = clientID;
        this.id = UUID.randomUUID().toString();
        this.totalMovement = 10;
        this.remainingMovement = totalMovement;
        this.abilities = new LinkedList<SlimeLordAbility>();
        this.factories = new LinkedList<SlimeFactory>();
        this.factories.add(new SlimeFactory(this.clientID));    // Austin, what is this?
        this.factories.add(new SlimeFactory(this.clientID));

        this.turn = new Turn(clientID);

        this.initializeAnimations();
//        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME1_RSC));
//        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME2_RSC));
//        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME3_RSC));
//        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME4_RSC));
    }

    void initializeAnimations() {
        String imageName = GREEN_IDLE;

        switch (this.color) {
            case "blue":
                imageName = BLUE_IDLE;
                break;
            case "green":
                imageName = GREEN_IDLE;
                break;
            case "yellow":
                imageName = YELLOW_IDLE;
                break;
            case "red":
                imageName = RED_IDLE;
                break;
        }

        Image idle = ResourceManager.getImage(imageName);
        idle.setFilter(Image.FILTER_NEAREST);
        SpriteSheet idleSheet = new SpriteSheet(idle, 64, 32);
        putAnimation("idle", new Animation(idleSheet, 99999));
        putAnimation("victory", new Animation(idleSheet, 250));
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

    @Override
    public void onAlterGameState(IGameState gameState) {

    }

    @Override
    public void onAlterPlayerState(Player player) {

    }

    @Override
    public void onCreateEntity(IEntity entity) {

    }

    @Override
    public void onDeleteEntity(int id) {

    }

    @Override
    public void onMessage(int senderId, String message) {

    }

    @Override
    public void onSetStateToBattle(SlimeLord lordOne, SlimeLord lordTwo) {

    }

    @Override
    public void onSetStateToOverworld() {

    }

    @Override
    public void onEndTurn() {
        turn.turnHasEnded();
    }

    @Override
    public void onConnectionConfirmation(int myId) {

    }

    @Override
    public void onLobbyClientListUpdate(String[] clientNames) {

    }

    @Override
    public void onLobbyIsFull() {

    }
}
