package game.Battles;

import game.entities.slime.Slime;
import game.entities.slimelord.SlimeLord;
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

    public void render(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.gray);
        g.fillRect(position.getX(), position.getY(), width, height);
        healthBar.render(g);

        g.setColor(c);
    }
}
