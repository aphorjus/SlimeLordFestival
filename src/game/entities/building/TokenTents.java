package game.entities.building;

import game.client.Board;
import jig.Vector;
import org.newdawn.slick.Graphics;

import jig.Entity;
import jig.ResourceManager;

public class TokenTents extends Entity {

    public int owner;
    public Vector cameraOffset = new Vector(0, 0);

    public TokenTents(final int owner, final int x, final int y) {
        super((float) x, (float) y);
        this.owner = owner;

        addImageWithBoundingBox(ResourceManager.getImage(Board.OPEN_TOKENTENT));
        addImageWithBoundingBox(ResourceManager.getImage(Board.GREEN_TOKENTENT));
        addImageWithBoundingBox(ResourceManager.getImage(Board.BLUE_TOKENTENT));
        addImageWithBoundingBox(ResourceManager.getImage(Board.RED_TOKENTENT));
        addImageWithBoundingBox(ResourceManager.getImage(Board.YELLOW_TOKENTENT));

    }

    public void setCameraOffset(Vector offset) {
        cameraOffset = offset;
    }

    public void positionForCamera() {
        translate(-cameraOffset.getX(), -cameraOffset.getY());
    }

    public void positionToOrigin() {
        translate(cameraOffset.getX(), cameraOffset.getY());
    }


    public void render(Graphics g) {
        float x = getX();
        float y = getY();
        if(owner == 0) {
            g.drawImage(ResourceManager.getImage(Board.BLUE_TOKENTENT), x, y);
        }
        if(owner == 1) {
            g.drawImage(ResourceManager.getImage(Board.GREEN_TOKENTENT), x, y);
        }
        if(owner == 2) {
            g.drawImage(ResourceManager.getImage(Board.YELLOW_TOKENTENT), x, y);
        }
        if(owner == 3) {
            g.drawImage(ResourceManager.getImage(Board.RED_TOKENTENT), x, y);
        }
        if(owner == 4) {
            g.drawImage(ResourceManager.getImage(Board.OPEN_TOKENTENT), x, y);
        }
    }
}
