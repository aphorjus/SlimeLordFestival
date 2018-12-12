package game.Battles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class HealthBar {

    float maxHealth;
    float currentHealth;
    float height;
    float width;
    float xpos;
    float ypos;

    float buffer = 5;

    Color outerColor = Color.black;
    Color inerColor = Color.gray;
    Color healthColor = Color.green;

    public HealthBar( float xpos, float ypos, float width, float height,
                      int currentHealth, int maxHealth){

        this.xpos = xpos;
        this.ypos = ypos;
        this.width = width;
        this.height = height;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;

    }

    public void setBuffer(float buffer){
        this.buffer = buffer;
    }

    public void setCurrentHealth(int currentHealth){
        this.currentHealth = currentHealth;
    }

    public void render(Graphics g){

        Color c = g.getColor();

        float healthWidth = width-(2*buffer);
        float healthHeight = height-(2*buffer);

        g.setColor(outerColor);
        g.fillRect(xpos, ypos, width, height);

        g.setColor(inerColor);
        g.fillRect(xpos+buffer, ypos+buffer, healthWidth, healthHeight);

        g.setColor(healthColor);
        g.fillRect(xpos+buffer, ypos+buffer, healthWidth*(currentHealth/maxHealth), healthHeight);

        g.setColor(c);
    }

}
