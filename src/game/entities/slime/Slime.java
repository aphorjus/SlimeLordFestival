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

public class Slime extends Entity implements IEntity, BattleEntity {

    public String entityType = "Slime";
    public int clientID;
    public String id;

    private int xIndex;
    private int yIndex;

    public int maxHP;
    public int currentHP;

    public int maxCooldown = 1;
    public int currentCooldown;

    public int size;
    public int damage;
    public double speed;
    public double minAttackRange = 0;
    public double maxAttackRange = (float)1.5;

    public boolean hasMoved = false;
    public boolean hasAttacked = false;
    public boolean upgraded = false;

    private boolean hasDirectionAttak;

    private final static int DOWN = 0;
    private final static int UP = 1;
    private final static int RIGHT = 2;
    private final static int LEFT = 3;
    private final static int DOWN_RIGHT = 4;
    private final static int DOWN_LEFT = 5;
    private final static int UP_RIGHT = 6;
    private final static int UP_LEFT = 7;

    private IntVector[][] attackPattern;

    private IntVector[] basicAttackPattern = {
            new IntVector(0,0)
    };

    private IntVector[][] morter = {{
            new IntVector(0,0),
            new IntVector(0,-1),
            new IntVector(0,1),
            new IntVector(-1,0),
            new IntVector(1,0)
    }};

    private IntVector[][] line = {
            { new IntVector(0,0), new IntVector( 0, 1), new IntVector( 0, 2)},
            { new IntVector(0,0), new IntVector( 0,-1), new IntVector( 0,-2)},
            { new IntVector(0,0), new IntVector( 1, 0), new IntVector( 2, 0)},
            { new IntVector(0,0), new IntVector(-1, 0), new IntVector(-2, 0)}
    };

    private IntVector[][] shortLine = {
            { new IntVector(0,0), new IntVector( 0, 1) },
            { new IntVector(0,0), new IntVector( 0,-1) },
            { new IntVector(0,0), new IntVector( 1, 0) },
            { new IntVector(0,0), new IntVector(-1, 0) }
    };

    private IntVector[][] shotgun = {
            { new IntVector(0,0), new IntVector( 0, 1), new IntVector( 1, 1), new IntVector(-1, 1) },
            { new IntVector(0,0), new IntVector( 0,-1), new IntVector(-1,-1), new IntVector( 1,-1) },
            { new IntVector(0,0), new IntVector( 1, 0), new IntVector( 1,-1), new IntVector( 1, 1) },
            { new IntVector(0,0), new IntVector(-1, 0), new IntVector(-1, 1), new IntVector(-1,-1) }
    };

    private IntVector[][] spread = {
            { new IntVector(0,0), new IntVector( 1, 1), new IntVector( 2, 2), new IntVector( 0, 2), new IntVector(-2, 2), new IntVector(-1, 1) },
            { new IntVector(0,0), new IntVector(-1,-1), new IntVector(-2,-2), new IntVector( 0,-2), new IntVector( 2,-2), new IntVector( 1,-1) },
            { new IntVector(0,0), new IntVector( 1,-1), new IntVector( 2,-2), new IntVector( 2, 0), new IntVector( 2, 2), new IntVector( 1, 1) },
            { new IntVector(0,0), new IntVector(-1, 1), new IntVector(-2, 2), new IntVector(-2, 0), new IntVector(-2,-2), new IntVector(-1,-1) }
    };

    public Slime(int size, int id){

        this.clientID = id;
        this.id = UUID.randomUUID().toString();
        this.size = size;
        this.speed = (int)((10/size) + 1);
        this.maxHP = 10*size;
        this.currentHP = maxHP;
        this.damage = 4+3*size;

        hasDirectionAttak = false;
        attackPattern = morter;

        setRec();
    }

    public Slime(JSONObject jsonSlime){

        this.clientID = jsonSlime.getInt("clientID");
        this.id = jsonSlime.getString("id");
        this.maxHP = jsonSlime.getInt("maxHP");
        this.currentHP = jsonSlime.getInt("currentHP");
        this.speed = jsonSlime.getDouble("speed");
        this.minAttackRange = jsonSlime.getDouble("minAttackRange");
        this.maxAttackRange = jsonSlime.getDouble("maxAttackRange");
        this.damage = jsonSlime.getInt("damage");
        this.size = jsonSlime.getInt("size");

        this.hasMoved = jsonSlime.getBoolean("hasMoved");
        this.hasAttacked = jsonSlime.getBoolean("hasAttacked");

//        attackPattern = spread;
//        hasDirectionAttak = true;
//        makeMorter();
        this.makeMorter();

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
        jsonSlime.put("minAttackRange", minAttackRange);
        jsonSlime.put("maxAttackRange", maxAttackRange);

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

    public void makeMorter(){

        this.setUpgraded(true);

        this.setMaxHP(20);
        this.speed = 1.5;
        this.damage = 20;
        this.setAttackRange(6, 10.5);

        this.attackPattern = morter;
        this.hasDirectionAttak = false;
    }

    public void makeStriker(){

        this.setUpgraded(true);

        setMaxHP(15);
        this.speed = 6;
        this.damage = 6;
        this.setAttackRange(0,1);

        this.attackPattern = shotgun;
        this.hasDirectionAttak = true;
    }

    public void makeAdvancedStriker(){

        this.setUpgraded(true);

        setMaxHP(25);
        this.speed = 6;
        this.damage = 6;
        this.setAttackRange(0,1.5);

        this.attackPattern = shotgun;
        this.hasDirectionAttak = true;
    }

    public void makeLancer(){

        this.setUpgraded(true);

        setMaxHP(15);
        this.speed = 8;
        this.damage = 8;
        this.setAttackRange(0,1.5);

        this.attackPattern = shortLine;
        this.hasDirectionAttak = true;
    }

    public void makeAdvancedLancer(){

        this.setUpgraded(true);

        setMaxHP(25);
        this.speed = 8;
        this.damage = 10;

        this.attackPattern = line;
        this.hasDirectionAttak = true;

    }

    public void upgrade(String type){

        switch(type){
            case "morter":              makeMorter(); break;
            case "striker":             makeStriker(); break;
            case "advancedStriker":     makeAdvancedStriker(); break;
            case "lancer":              makeLancer(); break;
            case "advancedLancer":      makeAdvancedLancer(); break;

            default: System.err.println("Error: No such slime type.");
        }

    }

    @Override
    public void setIndexes(int x, int y) {
        xIndex = x;
        yIndex = y;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
        if(currentHP > maxHP){
            currentHP = maxHP;
        }
    }

    public boolean hasMoved(){
        return hasMoved;
    }
    public void setHasMoved(boolean moved){
        this.hasMoved = moved;
    }
    public boolean hasAttacked() {
        return hasAttacked;
    }
    public void setHasAttacked(boolean attacked){
        this.hasAttacked = attacked;
    }
    public boolean isUpgraded() {
        return upgraded;
    }
    public void setUpgraded(boolean upgraded) {
        this.upgraded = upgraded;
    }
//    public float getAttackRange() {
//        return attackRange;
//    }
    public double getSpeed() {
        return speed;
    }
    public void setAttackRange(double min, double max){
        this.minAttackRange = min;
        this.maxAttackRange = max;
    }
    public boolean inRange(double distance){
        return distance >= minAttackRange && distance <= maxAttackRange;
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

    private int getDirection(int x, int y){

        if(y > yIndex){
            if(x > xIndex){
                return DOWN_RIGHT;
            }
            if(x < xIndex){
                return DOWN_LEFT;
            }
            return DOWN;
        }
        if(y < yIndex){
            if(x > xIndex){
                return UP_RIGHT;
            }
            if(x < xIndex){
                return UP_LEFT;
            }
            return UP;
        }
        if(x > xIndex){
            return RIGHT;
        }
        if(x < xIndex){
            return LEFT;
        }
        return 0;
    }

    public ArrayList<IntVector> calculatePattern(int direction1, int direction2, IntVector position){

        ArrayList<IntVector> pattern = new ArrayList<>();
        IntVector[] ap = attackPattern[direction1];

        if(direction2 == -1){

            for (int i = 0; i < ap.length; i++) {
                pattern.add(new IntVector(position.x + ap[i].x, position.y + ap[i].y));
            }
        }
        else {
            IntVector[] other_ap = attackPattern[direction2];

            for (int i = 0; i < ap.length; i++) {
                pattern.add(new IntVector(position.x + ap[i].x + other_ap[i].x,
                        position.y + ap[i].y + other_ap[i].y));
            }
        }
        return pattern;

    }

    public ArrayList<IntVector> getAttackPattern(int x, int y){
        ArrayList<IntVector> pattern = new ArrayList<>();
        IntVector position = new IntVector(x,y);

        if(hasDirectionAttak) {
            int direction = getDirection(x, y);

            switch (direction) {
                case (UP_LEFT): { pattern = calculatePattern(UP, LEFT, position);
                    break;
                }
                case (UP_RIGHT): { pattern = calculatePattern(UP, RIGHT, position);
                    break;
                }
                case (DOWN_LEFT): { pattern = calculatePattern(DOWN, LEFT, position);
                    break;
                }
                case (DOWN_RIGHT): { pattern = calculatePattern(DOWN, RIGHT, position);
                    break;
                }
                default: { //( UP || DOWN || LEFT || RIGHT )
                    pattern = calculatePattern(direction, -1, position);
                    break;
                }
            }
        }
        else {
            pattern = calculatePattern(0, -1, position);
        }
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

