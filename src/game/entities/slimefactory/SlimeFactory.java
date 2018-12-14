package game.entities.slimefactory;

import game.Battles.BattleEntity;
import game.Battles.BattleGrid;
import game.Battles.BattleGridTile;
import game.client.Board;
import game.entities.IEntity;
import game.entities.slime.Slime;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.UUID;

public class SlimeFactory extends Entity implements IEntity, BattleEntity {
    String GREEN_RSC = "game/client/resource/slime-factory-green.png";
    String RED_RSC = "game/client/resource/slime-factory-red.png";
    String YELLOW_RSC = "game/client/resource/slime-factory-yellow.png";
    String BLUE_RSC = "game/client/resource/slime-factory-blue.png";

    String entityType = "Factory";
    public int clientID;
    String id;
    int maxHP;
    int currentHP;

    int xIndex;
    int yIndex;

    Slime slime;
    ArrayList<BattleGridTile> spawnableTiles;

    public SlimeFactory(int clientID){
        this.clientID = clientID;
        this.id = UUID.randomUUID().toString();
        this.slime = new Slime(1, clientID);

        this.maxHP = 50;
        this.currentHP = maxHP;

        initializeAnimation();
    }

    public SlimeFactory(JSONObject data) {
        entityType = data.getString("entityType");
        clientID = data.getInt("clientId");
        id = data.getString("id");
        maxHP = data.getInt("maxHP");
        currentHP = data.getInt("currentHP");

        if(data.has("spawnableTiles")) {
            JSONArray jsonTiles = data.getJSONArray("spawnableTiles");
            spawnableTiles = new ArrayList<>();

            for (int i = 0; i < jsonTiles.length(); i++) {
                spawnableTiles.add(new BattleGridTile(jsonTiles.getJSONObject(i)));
            }
        }

        initializeAnimation();
    }

    void initializeAnimation() {
        String color = BLUE_RSC;
        switch (clientID) {
            case 0:
                color = BLUE_RSC;
                break;
            case 1:
                color = GREEN_RSC;
                break;
            case 2:
                color = YELLOW_RSC;
                break;
            case 3:
                color = RED_RSC;
                break;
        }

        Image image = ResourceManager.getImage(color);
        image.setFilter(Image.FILTER_NEAREST);
        SpriteSheet sheet = new SpriteSheet(image, 32, 32);
        addAnimation(new Animation(sheet, 200), new Vector(0, -4));
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        data.put("entityType", entityType);
        data.put("clientId", clientID);
        data.put("id", id);
        data.put("maxHP", maxHP);
        data.put("currentHP", currentHP);

        JSONArray jsonTiles = new JSONArray();

        if(spawnableTiles != null) {
            for (BattleGridTile tile : spawnableTiles) {
                jsonTiles.put(tile.toJson());
            }
        }

        data.put("spawnableTiles", jsonTiles);

        return data;
    }

    public void setSpawnableTiles(ArrayList<BattleGridTile> ajacentTiles){

        this.spawnableTiles = ajacentTiles;
    }

    @Override
    public void setIndexes(int x, int y) {
        xIndex = x;
        yIndex = y;
    }

    public String getEntityType() {
        return entityType;
    }

    public boolean equals(SlimeFactory factory){
        return id.equals(factory.id);
    }

    public BattleGridTile spawnSlime(){

        BattleGridTile tile = null;

        for( int i = 0; i < spawnableTiles.size(); i++ ){
            if( !spawnableTiles.get(i).hasOccupent() ){
                spawnableTiles.get(i).addOccupent( new Slime(1, clientID) );
                tile = spawnableTiles.get(i);
                break;
            }
        }
        return tile;
    }

    @Override
    public void onNextTurn() {
        spawnSlime();
    }

    @Override
    public void takeDamage(int amount) {
        currentHP -= amount;
    }

    @Override
    public boolean isAlive() {
        return currentHP > 0;
    }

    @Override
    public int getClientID() {
        return clientID;
    }

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public int getCurrentHP() {
        return currentHP;
    }
}
