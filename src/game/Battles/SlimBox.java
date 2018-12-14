package game.Battles;

import game.client.Button;
import game.entities.slime.Slime;
import game.entities.slimelord.SlimeLord;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import sun.awt.image.ImageWatched;

import java.util.LinkedList;

public class SlimBox {

    int width = 100;
    int height = 70;
    Vector position;

    int damage;
    int cooldown;
    int size;
    int maxHP;
    int currentHP;

    //    Slime slime;
    HealthBar healthBar;
    LinkedList<String> availableUpgrades;
    LinkedList<Button> upgradeButtons;


    public SlimBox( Slime slime, SlimeLord slimeLord ){

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

    }

    public void initButtons(){
        for( int i = 0; i < availableUpgrades.size(); i++ ){

            switch( availableUpgrades.get(i) ){

                case "basic":
                    upgradeButtons.add(new Button(position.getX(), position.getY()+(16*i), ResourceManager.getImage(  )));
                    break;
                case "mortar":              ;           break;
                case "striker":             ;          break;
                case "lancer":              ;           break;
                case "advancedStriker":     ;  break;
                case "advancedLancer":      ;   break;

            }

        }
    }

    public void render(Graphics g){

        Color c = g.getColor();
        g.setColor(Color.gray);
        g.fillRect(position.getX(), position.getY(), width, height);
        healthBar.render(g);

        g.setColor(c);
    }
}

