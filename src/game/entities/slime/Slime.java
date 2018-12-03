package game.entities.slime;

import game.Battles.BattleEntity;
import game.Battles.BattleGridTile;
import game.IntVector;
import game.entities.IEntity;
import game.client.Board;
import jig.Entity;
import jig.ResourceManager;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

public class Slime extends Entity implements IEntity, BattleEntity {

    public String entityType = "Slime";
    public int clientID;
    public String id;

    public int maxHP;
    public int currentHP;

    public int maxCooldown = 1;
    public int currentCooldown;

    public int size;
    public int damage;
    public float speed;
    public float attackRange = (float)1.5;

    public boolean hasMoved = false;
    public boolean hasAttacked = false;

    public Slime(int size, int id){

        this.clientID = id;
        this.id = UUID.randomUUID().toString();
        this.size = size;
        this.speed = (int)((10/size) + 1);
        this.maxHP = 10*size;
        this.currentHP = maxHP;
        this.damage = 4+3*size;

        setRec();
    }

    public Slime(JSONObject jsonSlime){

        this.clientID = jsonSlime.getInt("clientID");
        this.id = jsonSlime.getString("id");
        this.maxHP = jsonSlime.getInt("maxHP");
        this.currentHP = jsonSlime.getInt("currentHP");
        this.speed = jsonSlime.getFloat("speed");
        this.attackRange = jsonSlime.getFloat("attackRange");
        this.damage = jsonSlime.getInt("damage");
        this.size = jsonSlime.getInt("size");

        this.hasMoved = jsonSlime.getBoolean("hasMoved");
        this.hasAttacked = jsonSlime.getBoolean("hasAttacked");

        this.setRec();
    }

    @Override
    public JSONObject toJson() {

        JSONObject jsonSlime = new JSONObject();

        jsonSlime.put("entityType", getEntityType());
        jsonSlime.put("clientID", clientID);
        jsonSlime.put("id", id);
        jsonSlime.put("maxHP", maxHP);
        jsonSlime.put("currentHP", currentHP);
        jsonSlime.put("size", size);
        jsonSlime.put("damage", damage);
        jsonSlime.put("speed", speed);
        jsonSlime.put("attackRange", attackRange);

        jsonSlime.put("hasMoved", hasMoved);
        jsonSlime.put("hasAttacked", hasAttacked);

        setRec();

        return jsonSlime;
    }

    private void setRec(){
        switch(clientID) {
            case 0: this.addImage(ResourceManager.getImage(Board.SLIME1_RSC)); break;
            case 1: this.addImage(ResourceManager.getImage(Board.SLIME2_RSC)); break;
            case 2: this.addImage(ResourceManager.getImage(Board.SLIME3_RSC)); break;
            case 3: this.addImage(ResourceManager.getImage(Board.SLIME4_RSC)); break;
        }
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    public boolean hasMoved(){
        return hasMoved;
    }
    public void setHasMoved(boolean moved){
        hasMoved = moved;
    }
    public boolean hasAttacked() {
        return hasAttacked;
    }
    public void setHasAttacked(boolean attacked){
        hasAttacked = attacked;
    }
    public float getAttackRange() {
        return attackRange;
    }

    public float getSpeed() {
        return speed;
    }

    public Slime combine(Slime slime){
        Slime combinedSlime = new Slime(this.size + slime.size, this.clientID );
        combinedSlime.currentHP = this.currentHP + slime.currentHP;
        return combinedSlime;
    }

    public int getCurrentCooldown(){
        return currentCooldown;
    }

    public boolean equals(Slime slime){
        System.out.println(id+", "+slime.id);
        return id.equals(slime.id);
    }
    public boolean onCooldown(){

        return currentCooldown > 0;
    }

    public ArrayList<IntVector> getAttackPattern(int x, int y){
        ArrayList<IntVector> pattern = new ArrayList<>();

        pattern.add(new IntVector(x,y));

        return pattern;
    }


    public void onMove() {
        setHasMoved(true);
    }
    public void onAttack() {
        setHasAttacked(true);
    }

    @Override
    public void onNextTurn(){

        if(hasMoved() || hasAttacked()){
            currentCooldown = maxCooldown;
            hasMoved = false;
            hasAttacked = false;
        }
        else{
            currentCooldown -= 1;
        }
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

