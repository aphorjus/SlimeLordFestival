package game.entities.slime;

import game.Battles.BattleGridTile;
import game.entities.IEntity;
import game.client.Board;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONObject;

import java.util.UUID;

public class Slime extends Entity implements IEntity {

    public String entityType = "Slime";
    public int clientID;
    public String id;
    public int hp;
    public int speed;
    public int size;
    public int cooldown = 2;
    public int cooldownRemaining = cooldown;

    public Slime(int size, int id){

        this.clientID = id;
        this.id = UUID.randomUUID().toString();
        this.size = size;
        this.speed = (int)((10/size) + 1);
        this.hp = 10*size;

        setRec();
    }

    public Slime(JSONObject jsonSlime){

        this.clientID = jsonSlime.getInt("clientID");
        this.id = jsonSlime.getString("id");
        this.hp = jsonSlime.getInt("hp");
        this.speed = jsonSlime.getInt("speed");
        this.size = jsonSlime.getInt("size");

        this.setRec();
    }

    @Override
    public JSONObject toJson() {

        JSONObject jsonSlime = new JSONObject();

        jsonSlime.put("entityType", getEntityType());
        jsonSlime.put("clientID", clientID);
        jsonSlime.put("id", id);
        jsonSlime.put("hp", hp);
        jsonSlime.put("speed", speed);
        jsonSlime.put("size", size);

        setRec();

        return jsonSlime;
    }

    private void setRec(){
        switch(clientID) {
            case 1: this.addImage(ResourceManager.getImage(Board.SLIME1_RSC)); break;
            case 2: this.addImage(ResourceManager.getImage(Board.SLIME2_RSC)); break;
            case 3: this.addImage(ResourceManager.getImage(Board.SLIME3_RSC)); break;
            case 4: this.addImage(ResourceManager.getImage(Board.SLIME4_RSC)); break;
        }
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    public Slime combine(Slime slime){

        return new Slime(this.size + slime.size, this.clientID );
    }
    public int getCooldownRemaining(){
        return cooldownRemaining;
    }

    public boolean equals(Slime slime){
        System.out.println(id+", "+slime.id);
        return id.equals(slime.id);
    }
    public boolean canMove(){
        return cooldownRemaining < 1;
    }
    public void onNextTurn(){
        cooldownRemaining -= 1;
    }
}

