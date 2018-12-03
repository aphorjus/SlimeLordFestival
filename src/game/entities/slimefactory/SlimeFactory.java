package game.entities.slimefactory;

import game.Battles.BattleEntity;
import game.Battles.BattleGrid;
import game.Battles.BattleGridTile;
import game.client.Board;
import game.entities.IEntity;
import game.entities.slime.Slime;
import jig.Entity;
import jig.ResourceManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class SlimeFactory extends Entity implements IEntity, BattleEntity {
    String entityType = "Factory";
    public int clientID;
    String id;
    int maxHP;
    int currentHP;

    Slime slime;
    ArrayList<BattleGridTile> spawnableTiles;

    public SlimeFactory(int clientID){
        this.clientID = clientID;
        this.id = UUID.randomUUID().toString();
        this.slime = new Slime(1, clientID);

        this.maxHP = 50;
        this.currentHP = maxHP;

        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    public SlimeFactory(JSONObject data) {
        entityType = data.getString("entityType");
        clientID = data.getInt("clientId");
        id = data.getString("id");
        maxHP = data.getInt("maxHP");
        currentHP = data.getInt("currentHP");

        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        data.put("entityType", entityType);
        data.put("clientId", clientID);
        data.put("id", id);
        data.put("maxHP", maxHP);
        data.put("currentHP", currentHP);

        return data;
    }

    public void setSpawnableTiles(ArrayList<BattleGridTile> ajacentTiles){

        this.spawnableTiles = ajacentTiles;
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
                System.out.println(((Slime)tile.getOccupent()).clientID);
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
}
