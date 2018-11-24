package game.client;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import jig.ResourceManager;

public class Board {
    public static final String OVERWORLD_RSC = "game/client/resource/overworld.png";
    public static final String TILE_RSC = "game/client/resource/tile.png";
    public static final String SLIME1_RSC = "game/client/resource/slime1.png";
    public static final String SLIME2_RSC = "game/client/resource/slime2.png";
    public static final String SLIME3_RSC = "game/client/resource/slime3.png";
    public static final String SLIME4_RSC = "game/client/resource/slime4.png";

    public static int NUMROWS = 50;
    public static int NUMCOLS = 200;
    public static int KEYBOARD_COUNTDOWN = 800;
    private int countdown = KEYBOARD_COUNTDOWN;
    private boolean acceptKeyboard = false;
    private final Tile[][] tiles;
    private Tile current;
    private float xoffset = 0;
    private float yoffset = 0;
    private int slimeID = 1;

    public Board() {
        tiles = new Tile[NUMROWS][NUMCOLS];
    }

    public void render(GameContainer gc, StateBasedGame sbg,
                       Graphics g) throws SlickException {
        float x1 = 0;
        float y1 = 0;
        float x2 = GameClient.ScreenWidth;
        float y2 = GameClient.ScreenHeight;
        float x11 = xoffset;
        float y11 = yoffset;
        float x21 = x2 + xoffset;
        float y21 = y2 + yoffset;

        g.drawImage(ResourceManager.getImage(OVERWORLD_RSC), x1, y1, x2, y2, x11, y11, x21, y21);
        for(int row = 0; row < NUMROWS; row++) {
            for(int col = 0; col < NUMCOLS; col++) {
                if(tiles[row][col] != null) {
                    tiles[row][col].setOffsets(xoffset, yoffset);
                    tiles[row][col].render(g);
                }
            }
        }
    }

    public void initialize() {
        ResourceManager.loadImage(OVERWORLD_RSC);
        ResourceManager.loadImage(TILE_RSC);
        ResourceManager.loadImage(SLIME1_RSC);
        ResourceManager.loadImage(SLIME2_RSC);
        ResourceManager.loadImage(SLIME3_RSC);
        ResourceManager.loadImage(SLIME4_RSC);
        place("3", 0, 0);
        placeDown(10);
        placeRight(20);
        placeDown(50);
        placeRight(100);
        place("3", 0, 0);
        place("1", 10, 16);
    }
    public boolean place(String contents, int row, int col) {
        if(tiles[row][col] == null) {
            tiles[row][col] = new Tile(contents, row, col);
            current = tiles[row][col];
            return true;
        }
        else {
            tiles[row][col].setContents(contents);
            current = tiles[row][col];
        }
        return false;
    }
    public boolean placeLeft(int n) {
        int row = current.getRow();
        int col = current.getCol();
        for(int i = 0; i < n; i++) {
            col--;
            if(col >= 0) {
                tiles[row][col] = new Tile(" ", row, col);
                tiles[row][col].setRight(current);
                current.setLeft(tiles[row][col]);
                current = tiles[row][col];
            }
        }
        return true;
    }

    public boolean placeRight(int n) {
        int row = current.getRow();
        int col = current.getCol();
        for(int i = 0; i < n; i++) {
            col++;
            if(col < NUMCOLS) {
                tiles[row][col] = new Tile(" ", row, col);
                tiles[row][col].setLeft(current);
                current.setRight(tiles[row][col]);
                current = tiles[row][col];
            }
        }
        return true;
    }
    public boolean placeUp(int n) {
        int row = current.getRow();
        int col = current.getCol();
        for(int i = 0; i < n; i++) {
            row--;
            if(row >= 0) {
                tiles[row][col] = new Tile(" ", row, col);
                tiles[row][col].setDown(current);
                current.setUp(tiles[row][col]);
                current = tiles[row][col];
            }
        }
        return true;
    }

    public boolean placeDown(int n) {
        int row = current.getRow();
        int col = current.getCol();
        for(int i = 0; i < n; i++) {
            row++;
            if(row < NUMROWS) {
                tiles[row][col] = new Tile(" ", row, col);
                tiles[row][col].setUp(current);
                current.setDown(tiles[row][col]);
                current = tiles[row][col];
            }
        }
        return true;
    }

    public boolean moveLeft() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        if(current.getLeft() != null) {
            current.setContents("");
            current = current.getLeft();
            current.setContents("" + slimeID);
            return true;
        }
        return false;

    }

    public boolean moveRight() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        if(current.getRight() != null) {
            current.setContents("");
            current = current.getRight();
            current.setContents("" + slimeID);
            return true;
        }
        return false;

    }
    public boolean moveUp() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        if(current.getUp() != null) {
            current.setContents("");
            current = current.getUp();
            current.setContents("" + slimeID);
            return true;
        }
        return false;

    }

    public boolean moveDown() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        if(current.getDown() != null) {
            current.setContents("");
            current = current.getDown();
            current.setContents("" + slimeID);
            return true;
        }
        return false;

    }

    public boolean shiftRight() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        xoffset = Math.min(GameClient.ImageWidth - GameClient.ScreenWidth, xoffset + 5);
        return true;
    }

    public boolean shiftLeft() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        xoffset = Math.max(0, xoffset - 5);
        return true;
    }

    public boolean shiftUp() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        yoffset = Math.max(0, yoffset - 5);
        return true;
    }

    public boolean shiftDown() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        yoffset = Math.min(GameClient.ImageHeight - GameClient.ScreenHeight, yoffset + 5);
        return true;
    }

    public void update(int delta) {
        countdown = countdown - delta;
        if(countdown < 0) {
            countdown = KEYBOARD_COUNTDOWN;
            acceptKeyboard = true;
        }
    }
}

