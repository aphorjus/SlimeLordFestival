package game.Battles;

import game.client.Button;
import game.client.states.BattleState;
import game.entities.slime.Slime;
import game.entities.slimelord.SlimeLord;
import jig.ResourceManager;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import java.util.LinkedList;

public class SlimBox {

    public final static String BASIC_UB = "game/client/resource/basic-upgrade.png";
//    public final static String MORTAR_UB = "game/client/resource/mortar-upgrad.png";
    public final static String STRIKER_UB = "game/client/resource/striker-upgrade.png";
    public final static String LANCER_UB = "game/client/resource/lancer-upgrade.png";
    public final static String ADVANCEDSTRIKER_UB = "game/client/resource/advanced-striker-upgrade.png";
    public final static String ADVANCEDLANCER_UB = "game/client/resource/advanced-lancer-upgrade.png";

    public Image basic_ub;
//    public Image morter_ub;
    public Image striker_ub;
    public Image lancer_ub;
    public Image advancedstriker_ub;
    public Image advancedlancer_ub;

    int width = 100;
    int height = 70;
    Vector position;

    int damage;
    int cooldown;
    int size;
    int maxHP;
    int currentHP;

    boolean active = false;

    Slime slime;
    HealthBar healthBar;
    LinkedList<String> availableUpgrades;
    LinkedList<Button> upgradeButtons;

    public SlimBox(){
        try{
            basic_ub = new Image(BASIC_UB);
//            morter_ub = new Image(MORTAR_UB);
            lancer_ub = new Image(LANCER_UB);
            striker_ub = new Image(STRIKER_UB);
            advancedlancer_ub = new Image(ADVANCEDLANCER_UB);
            advancedstriker_ub = new Image(ADVANCEDSTRIKER_UB);

        } catch ( Exception e){
            e.printStackTrace();
        }
    }

    public SlimBox( Slime slime, SlimeLord slimeLord ){

        try{
            basic_ub = new Image(BASIC_UB);
//            morter_ub = new Image(MORTAR_UB);
            lancer_ub = new Image(LANCER_UB);
            striker_ub = new Image(STRIKER_UB);
            advancedlancer_ub = new Image(ADVANCEDLANCER_UB);
            advancedstriker_ub = new Image(ADVANCEDSTRIKER_UB);

        } catch ( Exception e){
            e.printStackTrace();
        }

        this.position = slime.getPosition();

        this.maxHP = slime.maxHP;
        this.currentHP = slime.currentHP;
        this.damage = slime.damage;
        this.cooldown = slime.currentCooldown;
        this.size = slime.size;

        this.healthBar = new HealthBar(position.getX()+10, position.getY()+10,
                width-20, 20, currentHP, maxHP);

        if( slime.clientID == slimeLord.clientID ) {
            this.availableUpgrades = slime.getAvailableUpgrades(slimeLord.specialSlimes);
        }
        initButtons();
    }

    public void updateBox( Slime slime, SlimeLord slimeLord ){

        this.slime = slime;
        this.position = slime.getPosition();

        this.maxHP = slime.maxHP;
        this.currentHP = slime.currentHP;
        this.damage = slime.damage;
        this.cooldown = slime.currentCooldown;
        this.size = slime.size;

        this.healthBar = new HealthBar(position.getX()+10, position.getY()+10,
                width-20, 20, currentHP, maxHP);

        if( slime.clientID == slimeLord.clientID ) {
            this.availableUpgrades = slime.getAvailableUpgrades(slimeLord.specialSlimes);
            initButtons();
        }
    }

    public void initButtons(){

        upgradeButtons = new LinkedList<>();

        for( int i = 0; i < availableUpgrades.size(); i++ ){

            switch( availableUpgrades.get(i) ){
                case "basic":
                    upgradeButtons.add(new Button((int)position.getX(),(int)position.getY()+(16*i)+30,
                            basic_ub));
                    break;
                case "mortar":
                    upgradeButtons.add(new Button((int)position.getX(),(int)position.getY()+(16*i)+30,
                            lancer_ub));
                    break;
                case "striker":
                    upgradeButtons.add(new Button((int)position.getX(),(int)position.getY()+(16*i)+30,
                            striker_ub));
                    break;
                case "lancer":
                    upgradeButtons.add(new Button((int)position.getX(),(int)position.getY()+(16*i)+30,
                            lancer_ub));
                    break;
                case "advancedStriker":
                    upgradeButtons.add(new Button((int)position.getX(),(int)position.getY()+(16*i)+30,
                            advancedstriker_ub));
                    break;
                case "advancedLancer":
                    upgradeButtons.add(new Button((int)position.getX(),(int)position.getY()+(16*i)+30,
                            advancedlancer_ub));
                    break;
                default:
                    System.err.println("Invalid upgrade type: "+availableUpgrades.get(i));
            }
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void update(int x, int y){

        if(!active){
            return;
        }

        for( int i = 0; i < upgradeButtons.size(); i++ ){
            if(upgradeButtons.get(i).checkClick( x, y )){
                System.out.println(availableUpgrades.get(i));
                slime.upgradeTo(availableUpgrades.get(i));
                return;
            }
        }
    }

    public void render(Graphics g){

        if(!active){
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.gray);
        g.fillRect(position.getX(), position.getY(), width, height);
        healthBar.render(g);

        for( int i = 0; i < upgradeButtons.size(); i++ ){
            upgradeButtons.get(i).render(g);
        }
        g.setColor(c);
    }
}

