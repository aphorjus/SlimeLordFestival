package game.Battles;

import game.entities.IEntity;
import org.json.JSONObject;

public class HealthManager implements IEntity {

    String entityType = "HealthManager";
    int maxhp;
    int currenthp;

    public HealthManager(int hp){
        this.maxhp = hp;
        this.currenthp = maxhp;
    }

    public void damage(int amount){
        currenthp -= amount;
    }

    public void heal(int amount){
        currenthp += amount;

        if(currenthp > maxhp){
            currenthp = maxhp;
        }
    }

    public boolean isDead(){
        return currenthp <= 0;
    }

    @Override
    public String getEntityType(){
        return entityType;
    }

    @Override
    public JSONObject toJson(){

        JSONObject data  = new JSONObject();
        return data;

    }

}
