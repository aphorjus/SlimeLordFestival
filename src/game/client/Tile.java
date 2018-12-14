package game.client;

import game.entities.slimelord.SlimeLord;
import jig.Vector;
import org.newdawn.slick.Graphics;

import jig.Entity;
import jig.ResourceManager;

// Tile class that deals with player 1's Tiles and player 2's Tiles
public class Tile extends Entity {

    private int row;
    private int col;
    private String contents;
    public Tile left;
    public Tile right;
    public Tile up;
    public Tile down;
    private float xoffset = 0;
    private float yoffset = 0;
    public boolean visited;
    public boolean isHighlighted;
    public SlimeLord heldSlimeLord;

    public Tile(final String contents, final int row, final int col) {
        super((float) col*16, (float) row*16);
        this.contents = contents;
        this.row = row;
        this.col = col;

        addImageWithBoundingBox(ResourceManager.getImage(Board.TILE_RSC));
        addImageWithBoundingBox(ResourceManager.getImage(Board.HIGHLIGHTED_TILE_RSC));
    }

    public void setHeldSlimeLord(SlimeLord lord) {
        heldSlimeLord = lord;
    }

    public void render(Graphics g, Vector cameraOffset) {
        float x = getX() - xoffset;
        float y = getY() - yoffset;

        if(isHighlighted) {
            g.drawImage(ResourceManager.getImage(Board.HIGHLIGHTED_TILE_RSC), x, y);
        } else if(contents.startsWith("T")) {
            int id = Integer.parseInt(contents.split(":")[1]);
        } else {
            g.drawImage(ResourceManager.getImage(Board.TILE_RSC), x, y);
        }


        if (heldSlimeLord != null) {
            heldSlimeLord.setCameraOffset(cameraOffset);
            heldSlimeLord.positionForCamera();
            heldSlimeLord.render(g);
            heldSlimeLord.positionToOrigin();
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Tile getLeft() {
        return left;
    }

    public void setLeft(Tile left) {
        this.left = left;
    }

    public Tile getRight() {
        return right;
    }

    public void setRight(Tile right) {
        this.right = right;
    }

    public Tile getUp() {
        return up;
    }

    public void setUp(Tile up) {
        this.up = up;
    }

    public Tile getDown() {
        return down;
    }

    public void setDown(Tile down) {
        this.down = down;
    }

    public void update(final int delta) {

    }

    public void setOffsets(float xoffset, float yoffset) {
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }
}
