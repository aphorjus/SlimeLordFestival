package game.entities.slimelord;

import game.Battles.AttackPattern;
import game.Battles.BattleEntity;
import game.Battles.BattleGridTile;
import game.IntVector;
import game.api.GameApi;
import game.entities.IEntity;
import game.entities.slime.Slime;
import game.entities.slimefactory.SlimeFactory;

import java.util.ArrayList;
import java.util.LinkedList;


public class BattleAbility extends SlimeLordAbility {

    private GameApi gameApi;
    private AttackPattern attackPattern = new AttackPattern();

    private boolean selected;
    private boolean used;

    private int currentPlayerId;

    public static int TARGETED_EFFECT = 1;
    public static int MASS_EFFECT = 2;

    // Variables to be set for "damage" effectType
    private int ablityDamage;
    private boolean effectsFriendlies;
    private boolean effectsEnEnemies;
    private boolean effectsFactories;
    //

    // Variables to be set for "summon" effectType
    private String summonType;
    //

    // Variables to be set for both
    private int targetingType;
    private String effectType;


    public BattleAbility(GameApi gameApi){
        this.gameApi = gameApi;
        return;
    }
    public BattleAbility(){
        return;
    }

    public boolean used(){
        return used;
    }

    public boolean selected(){
        return selected;
    }

    //                                                       //
    //                 ABILITY SELECTION                     //
    //   This is where an abilities attributes are defined   //

    public void selectAbility(String ability){

       switch(ability){
           case "damage":           selectDamage();             break;
           case "slimeStrike":      selectSlimeStrike();        break;
           case "slimeBall":        selectSlimeBall();          break;
           case "massHeal":         selectMassHeal();
           case "summonBasicSlime": selectSummonBasicSlime();   break;
           case "summonLancer":     selectSummonLancer();       break;
           default: System.err.println("No battle ability "+ability); return;
       }
       selected = true;
    }

    private void selectDamage(){

        effectType = "damage";

        targetingType = TARGETED_EFFECT;
        attackPattern.set(AttackPattern.MORTAR, false);

        effectsEnEnemies = true;
        effectsFriendlies = true;
        effectsFactories = true;

        ablityDamage = 500;
    }

    private void selectSlimeStrike(){

        effectType = "damage";

        targetingType = TARGETED_EFFECT;
        attackPattern.set(AttackPattern.SINGLE_TARGET, false);

        effectsEnEnemies = true;
        effectsFriendlies = false;
        effectsFactories = false;

        ablityDamage = 25;
    }

    private void selectSlimeBall(){

        effectType = "damage";

        targetingType = TARGETED_EFFECT;
        attackPattern.set(AttackPattern.SQUARE, false);

        effectsEnEnemies = true;
        effectsFriendlies = true;
        effectsFactories = false;

        ablityDamage = 10;
    }

    private void selectMassHeal(){

        effectType = "damage";

        targetingType = MASS_EFFECT;

        effectsEnEnemies = false;
        effectsFriendlies = true;
        effectsFactories = true;

        ablityDamage = -15;
    }

    private void selectSummonBasicSlime(){

        effectType = "summon";

        targetingType = TARGETED_EFFECT;
        attackPattern.set(AttackPattern.LINE, false);
        summonType = "basic";
    }

    private void selectSummonLancer(){

        effectType = "summon";

        targetingType = TARGETED_EFFECT;
        attackPattern.set(AttackPattern.SINGLE_TARGET, false);
        summonType = "advancedLancer";
    }


    //                      //
    //  ABILITY ACTIVATION  //
    //                      //

    public void activateAbility( ArrayList<BattleGridTile> effected ){
        if( selected && !used ){
            switch(effectType){
                case "damage": activateDamage(effected); break;
                case "summon": activateSummon(effected); break;
            }
        }

    }

    private boolean effectsThis( BattleGridTile tile ){

        BattleEntity entity = tile.getOccupent();

        if( entity == null){
            return false;
        }
        if(entity instanceof SlimeFactory && !effectsFactories){
            System.err.println("this ability dose noe effect factories");
            return false;
        }
        if( entity.getClientID() == currentPlayerId && !effectsFriendlies){
            System.err.println("this ability dose noe effect friendlies");
            return false;
        }
        else if( entity.getClientID() != currentPlayerId && !effectsEnEnemies){
            System.err.println("this ability dose noe effect enemies");
            return false;
        }
        return true;

    }

    private void activateDamage( ArrayList<BattleGridTile> effectedTiles ) {

        for( int i = 0; i < effectedTiles.size(); i++){
            BattleGridTile effectedTile = effectedTiles.get(i);

            if( effectsThis( effectedTile ) ){
                effectedTile.damageOccupent(ablityDamage);
            }
        }
    }

    private void activateSummon( ArrayList<BattleGridTile> effectedTiles ){

        for( int i = 0; i < effectedTiles.size(); i++) {
            BattleGridTile effectedTile = effectedTiles.get(i);

            if (!effectedTile.hasOccupent()) {
                Slime summon = new Slime(1, currentPlayerId);
                summon.upgradeTo(summonType);
                effectedTile.addOccupent(summon);
            }
        }
    }

    public int getTargetingType() {
        return targetingType;
    }

    public ArrayList<IntVector> getAttackPattern(int x, int y) {
        return attackPattern.getAttackPattern(x, y, x, y);
    }

    public int getAblityDamage() {
        return ablityDamage;
    }

    public void onEndTurn(int activePlayer){

        currentPlayerId = activePlayer;
        selected = false;
        used = false;
    }
}
