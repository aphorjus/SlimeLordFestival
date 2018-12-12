package game.Battles;

import game.entities.slime.Slime;
import org.newdawn.slick.Graphics;

public class SlimBox {

    int width = 100;
    int height = 50;
    Slime slime;
    HealthBar healthBar;


    public SlimBox( Slime slime ){

        this.slime = slime;
        this.healthBar = new HealthBar(slime.getX(), slime.getY(),
                width, height, slime.currentHP, slime.maxHP);
    }


    public void render(Graphics g){
        g.fillRect(slime.getX(), slime.getY(), width, height);
        healthBar.render(g);
    }
}
