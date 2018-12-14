package game.entities.slime;

import game.Battles.AttackPattern;
import game.Battles.BattleEntity;
import game.Battles.BattleGridTile;
import game.IntVector;
import game.entities.AnimatedEntity;
import game.entities.IEntity;
import game.client.Board;
import game.utility.AnimationSwitchTimer;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class Slime extends AnimatedEntity implements IEntity, BattleEntity {

    String GREEN_IDLE = "game/client/resource/green-slime-idle.png";
    String GREEN_ATTACK = "game/client/resource/green-slime-attack.png";
    String GREEN_DEATH = "game/client/resource/green-slime-death.png";

    String RED_IDLE = "game/client/resource/red-slime-idle.png";
    String RED_ATTACK = "game/client/resource/red-slime-attack.png";
    String RED_DEATH = "game/client/resource/red-slime-death.png";

    String YELLOW_IDLE = "game/client/resource/yellow-slime-idle.png";
    String YELLOW_ATTACK = "game/client/resource/yellow-slime-attack.png";
    String YELLOW_DEATH = "game/client/resource/yellow-slime-death.png";

    String BLUE_IDLE = "game/client/resource/blue-slime-idle.png";
    String BLUE_ATTACK = "game/client/resource/blue-slime-attack.png";
    String BLUE_DEATH = "game/client/resource/blue-slime-death.png";

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
    public double minAttackRange;
    public double maxAttackRange;

    public boolean hasMoved = false;
    public boolean hasAttacked = false;
    public boolean upgraded = false;

    public String myType;

    private AttackPattern attackPattern = new AttackPattern();

    String color;

    public Slime(int size, int id){

//        setRec();
//        this.initializeAnimations();
        this.clientID = id;
        this.id = UUID.randomUUID().toString();

        this.size = size;

//        this.makeAdvancedStriker();
        this.makeBasic();
//        this.makeLancer();
//        this.upgradeTo("lancer");
        this.currentHP = maxHP;
        setRec();
        this.initializeAnimations();
    }

    public Slime(JSONObject jsonSlime){

        this.clientID =     jsonSlime.getInt("clientID");
        this.id =           jsonSlime.getString("id");
        this.currentHP =    jsonSlime.getInt("currentHP");
        this.size =         jsonSlime.getInt("size");
        this.hasMoved =     jsonSlime.getBoolean("hasMoved");
        this.hasAttacked =  jsonSlime.getBoolean("hasAttacked");
        this.myType =       jsonSlime.getString("myType");

        this.upgradeTo(myType);
        this.setRec();
        this.initializeAnimations();
    }

    void initializeAnimations() {
        String idleImageName = GREEN_IDLE;
        String attackImageName = GREEN_ATTACK;
        String deathImageName = GREEN_DEATH;

        switch (this.color) {
            case "green":
                idleImageName = GREEN_IDLE;
                attackImageName = GREEN_ATTACK;
                deathImageName = GREEN_DEATH;
                break;
            case "blue":
                idleImageName = BLUE_IDLE;
                attackImageName = BLUE_ATTACK;
                deathImageName = BLUE_DEATH;
                break;
            case "red":
                idleImageName = RED_IDLE;
                attackImageName = RED_ATTACK;
                deathImageName = RED_DEATH;
                break;
            case "yellow":
                idleImageName = YELLOW_IDLE;
                attackImageName = YELLOW_ATTACK;
                deathImageName = YELLOW_DEATH;
                break;
        }

        Vector offset = new Vector(0, -8);

        Image idleImage = ResourceManager.getImage(idleImageName);
        idleImage.setFilter(Image.FILTER_NEAREST);
        SpriteSheet idleSheet = new SpriteSheet(idleImage, 32, 32);
        putAnimation("idle", new Animation(idleSheet, 100), offset);

        Image attackImage = ResourceManager.getImage(attackImageName);
        attackImage.setFilter(Image.FILTER_NEAREST);
        SpriteSheet attackSheet = new SpriteSheet(attackImage, 32, 32);
        putAnimation("attack", new Animation(attackSheet, 100), offset);

        Image deathImage = ResourceManager.getImage(deathImageName);
        deathImage.setFilter(Image.FILTER_NEAREST);
        SpriteSheet deathSheet = new SpriteSheet(deathImage, 32, 32);
        putAnimation("death", new Animation(deathSheet, 100), offset);

        playAnimation("idle");
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonSlime = new JSONObject();

        jsonSlime.put("entityType", entityType);
        jsonSlime.put("clientID", clientID);
        jsonSlime.put("id", id);
        jsonSlime.put("currentHP", currentHP);
        jsonSlime.put("size", size);
        jsonSlime.put("hasMoved", hasMoved);
        jsonSlime.put("hasAttacked", hasAttacked);
        jsonSlime.put("myType", myType);

        setRec();

        return jsonSlime;
    }

    private void setRec(){
        switch(clientID) {
            case 0: this.color = "blue"; break;
            case 1: this.color = "green"; break;
            case 2: this.color = "red"; break;
            case 3: this.color = "yellow"; break;
        }
    }

    public void setMyType(String myType) {
        this.myType = myType;
    }

    @Override
    public int getClientID() {
        return clientID;
    }

    public void makeBasic(){

        this.setMyType("basic");
        this.setUpgraded(false);

        this.setMaxHP(10*size);
        this.speed = (int)((10/size) + 1);
        this.damage = 4+3*size;
        this.setAttackRange(0, 1.5);

        this.attackPattern.set(AttackPattern.SINGLE_TARGET, false);

    }

    public void makeMortar(){
        this.setMyType("mortar");
        this.setUpgraded(true);

        this.setMaxHP(20);
        this.speed = 1.5;
        this.damage = 20;
        this.setAttackRange(6, 10.5);

        this.attackPattern.set(AttackPattern.MORTAR, false);
    }

    public void makeStriker(){
        this.setMyType("striker");
        this.setUpgraded(true);

        setMaxHP(15);
        this.speed = 6;
        this.damage = 6;
        this.setAttackRange(0,1);

        this.attackPattern.set(AttackPattern.SHOTGUN, true);
    }

    public void makeAdvancedStriker(){
        this.setMyType("advancedStriker");
        this.setUpgraded(true);

        setMaxHP(24);
        this.speed = 6;
        this.damage = 8;
        this.setAttackRange(0,1.5);

        this.attackPattern.set(AttackPattern.SHOTGUN, true);
    }

    public void makeLancer(){
        this.setMyType("lancer");
        this.setUpgraded(true);

        setMaxHP(15);
        this.speed = 8;
        this.damage = 8;
        this.setAttackRange(0,1.5);

        this.attackPattern.set(AttackPattern.SHORT_LINE, true);
    }

    public void makeAdvancedLancer(){
        this.setMyType("advancedLancer");
        this.setUpgraded(true);

        setMaxHP(25);
        this.speed = 8;
        this.damage = 10;
        this.setAttackRange(0,1.5);

        this.attackPattern.set(AttackPattern.LINE, true);
    }

    public void upgradeTo(String type){

        switch(type){
            case "basic":               makeBasic();            break;
            case "mortar":              makeMortar();           break;
            case "striker":             makeStriker();          break;
            case "lancer":              makeLancer();           break;
            case "advancedStriker":     makeAdvancedStriker();  break;
            case "advancedLancer":      makeAdvancedLancer();   break;

            default: System.err.println("Error: No such slime type.");
        }
    }

    public LinkedList<String> getAvailableUpgrades(LinkedList<String> specialSlimes){

        LinkedList<String> availableUpgrades = new LinkedList<>();

        if ( isUpgraded() ){
            availableUpgrades.add("basic");
            return availableUpgrades;
        }
        if ( size >= 2 ){
            if(specialSlimes.contains("striker")){
                availableUpgrades.add("striker");
            }
            if(specialSlimes.contains("lancer")){
                availableUpgrades.add("lancer");
            }
        }
        if ( size >= 4 ){
            if(specialSlimes.contains("advancedStriker")) {
                availableUpgrades.add("advancedStriker");
            }
            if(specialSlimes.contains("advancedLancer")) {
                availableUpgrades.add("advancedLancer");
            }
        }
        if ( size >= 6 ){
            if(specialSlimes.contains("mortar")) {
                availableUpgrades.add("mortar");
            }
        }

        System.out.println(specialSlimes.toString());
        System.out.println(availableUpgrades.toString());

        for( int i = 0; i < specialSlimes.size(); i++ ){
            if( !availableUpgrades.contains(specialSlimes.get(i)) ){
                availableUpgrades.remove(specialSlimes.get(i));
            }
        }

        return availableUpgrades;
    }

    public ArrayList<IntVector> getAttackPattern(int x, int y) {
        return attackPattern.getAttackPattern(x, y, xIndex, yIndex);
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

    @Override
    public int getMaxHP() {
        return maxHP;
    }

    @Override
    public int getCurrentHP() {
        return currentHP;
    }
}

