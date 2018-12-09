package game.entities.slimelord;

import game.IGameState;
import game.api.GameApiListener;
import game.client.Board;
import game.client.Player;
import game.client.Turn;
import game.entities.IEntity;
import game.entities.slimefactory.SlimeFactory;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;

import java.util.LinkedList;
import java.util.UUID;

public class SlimeLord extends Entity implements IEntity, GameApiListener {
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

        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME1_RSC));
        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME2_RSC));
        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME3_RSC));
        addImageWithBoundingBox(ResourceManager.getImage(Board.SLIME4_RSC));
    }

    public void render(Graphics g){
        float x = getX() - xoffset;
        float y = getY() - yoffset;
        if(clientID == 0) {
            g.drawImage(ResourceManager.getImage(Board.SLIME1_RSC), x + 1, y + 1);
        }
        if(clientID == 1) {
            g.drawImage(ResourceManager.getImage(Board.SLIME2_RSC), x + 1, y + 1);
        }
        if(clientID == 2) {
            g.drawImage(ResourceManager.getImage(Board.SLIME3_RSC), x + 1, y + 1);
        }
        if(clientID == 3) {
            g.drawImage(ResourceManager.getImage(Board.SLIME4_RSC), x + 1, y + 1);
        }
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
        System.out.println("JSONObject");
        try {

            abilities = new LinkedList<>();
            factories = new LinkedList<>();

            entityType = data.getString("entityType");
            clientID = data.getInt("clientID");
            id = data.getString("id");
            name = data.getString("name");
            totalMovement = data.getInt("totalMovement");
            remainingMovement = data.getInt("remainingMovement");

            System.out.println("system created");

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
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public String getEntityType() {
        return entityType;
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        System.out.println("toJson.");
        try {
            data.put("entityType", entityType);
            data.put("clientID", clientID);
            data.put("id", id);
            data.put("name", name);
            data.put("totalMovement", totalMovement);
            data.put("remainingMovement", remainingMovement);
            System.out.println(abilities.size());
            System.out.println(factories.size());


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
                    System.out.println(factory.toJson());
                    jsonFactories.put(factory.toJson());
                }

                data.put("factories", jsonFactories);
            }

            System.out.println("json created.");
        } catch (Exception ex) {
            ex.printStackTrace();
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
