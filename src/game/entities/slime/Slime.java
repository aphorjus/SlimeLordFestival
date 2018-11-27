package game.entities.slime;

import game.Battles.BattleGridTile;
import game.entities.IEntity;
import game.client.Board;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONObject;

public class Slime extends Entity implements IEntity {

    public String entityType = "Slime";
    public int clientID;
    public int hp;
    public int speed;
    public int size;

    public Slime(int size, Vector position, int id){
        super(position);
        this.clientID = id;
        this.size = size;
        this.speed = (int)((10/size) + 1);
        this.hp = 10*size;

        this.addImage(ResourceManager.getImage(Board.SLIME1_RSC));
    }

    public Slime(JSONObject jsonSlime){

        this.clientID = jsonSlime.getInt("clientID");
        this.hp = jsonSlime.getInt("hp");
        this.speed = jsonSlime.getInt("speed");
        this.size = jsonSlime.getInt("size");

        this.addImage(ResourceManager.getImage(Board.SLIME1_RSC));

    }

    @Override
    public JSONObject toJson() {

        JSONObject jsonSlime = new JSONObject();

        jsonSlime.put("entityType", getEntityType());
        jsonSlime.put("clientID", clientID);
        jsonSlime.put("hp", hp);
        jsonSlime.put("speed", speed);
        jsonSlime.put("size", size);

        return jsonSlime;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    public Slime combine(Slime slime){

        return new Slime(this.size + slime.size, slime.getPosition(), this.clientID );
    }
}

