package game.entities.slimelord;

import game.Battles.AttackPattern;
import game.Battles.BattleEntity;
import game.Battles.BattleGridTile;
import game.IntVector;
import game.api.GameApi;
import game.entities.IEntity;
import game.entities.slime.Slime;
import java.util.ArrayList;
import java.util.LinkedList;


public class BattleAbility extends SlimeLordAbility {

    private GameApi gameApi;
    private AttackPattern attackPattern = new AttackPattern();

    private boolean selected;
    private boolean used;

    private int ablityDamage;
    private int effectType;
    public String currentlySelectedAbility;

    public static int TARGETED_EFFECT = 1;
    public static int MASS_EFFECT = 2;

    public BattleAbility(GameApi gameApi){
        this.gameApi = gameApi;
        return;
    }
    public BattleAbility(){
        return;
    }

    public void selectAbility(String ability){

       switch(ability){
           case "damage": selectDamage("damage"); break;
           default: System.err.println("No battle ability "+ability);
       }
       selected = true;
    }

    private void selectDamage(String name){

        currentlySelectedAbility = name;

        effectType = TARGETED_EFFECT;
        attackPattern.set(AttackPattern.MORTAR, false);
        ablityDamage = 50;
        used = false;
    }

    public void activateAbility( BattleGridTile effected ){
        if( selected && !used ){
            switch(currentlySelectedAbility){
                case "damage": activateDamage(effected); return;
            }
        }
        used = true;
    }

    private void activateDamage( BattleGridTile effectedTile ) {

        BattleEntity effected = effectedTile.getOccupent();

        if (effected instanceof Slime) {
            effectedTile.damageOccupent(ablityDamage);
        }
        else if (effected == null){
            System.err.println("No Target");
        }
        else{
            System.err.println("This ablilty dose not effect factories");
        }
    }

    public int getEffectType() {
        return effectType;
    }

    public ArrayList<IntVector> getAttackPattern(int x, int y) {
        return attackPattern.getAttackPattern(x, y, x, y);
    }

    public int getAblityDamage() {
        return ablityDamage;
    }
    public void onEndTurn(){
        selected = false;
        used = false;
    }
}
