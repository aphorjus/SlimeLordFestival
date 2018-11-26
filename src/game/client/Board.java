package game.client;

import javax.swing.JOptionPane;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import jig.ResourceManager;
import game.client.GameClient;
import game.client.Tile;

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

    public void render(GameContainer container, StateBasedGame game,
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

    // setting up tiles
    public void initialize() {
        ResourceManager.loadImage(OVERWORLD_RSC);
        ResourceManager.loadImage(TILE_RSC);
        ResourceManager.loadImage(SLIME1_RSC);
        ResourceManager.loadImage(SLIME2_RSC);
        ResourceManager.loadImage(SLIME3_RSC);
        ResourceManager.loadImage(SLIME4_RSC);
        // place = 0
        // placeUp = 1
        // placeRight = 2
        // placeDown = 3
        // placeLeft = 4

        int []placement =
                {
                        0, 10, 5,
                        3, 10, 0,
                        2, 8, 0,
                        1, 3, 0,
                        0, 20, 11,
                        3, 14, 0,
                        4, 6, 0,
                        3, 5, 0,
                        0, 34, 6,
                        1, 6, 0,
                        0, 34, 12,
                        2, 3, 0,
                        3, 6, 0,
                        2, 3, 0,
                        3, 3, 0,
                        0, 30, 11,
                        2, 11, 0,
                        3, 3, 0,
                        0, 30, 22,
                        2, 9, 0,
                        3, 2, 0,
                        0, 30, 31,
                        1, 2, 0,
                        0, 30, 31,
                        2, 6, 0,
                        3, 16, 0,
                        2, 11, 0,
                        0, 46, 45,
                        1, 6, 0,
                        2, 9, 0,
                        1, 1, 0,
                        0, 40, 54,
                        2, 6, 0,
                        1, 14, 0,
                        0, 40, 60,
                        2, 6, 0,
                        1, 2, 0,
                        0, 40, 66,
                        2, 7, 0,
                        1, 7, 0,
                        2, 8, 0,
                        3, 6, 0,
                        0, 33, 81,
                        2, 1, 0,
                        1, 3, 0,
                        0, 33, 77,
                        1, 7, 0,
                        4, 20, 0,
                        1, 3, 0,
                        0, 26, 70,
                        1, 7, 0,
                        2, 6, 0,
                        1, 3, 0,
                        0, 19, 70,
                        4, 6, 0,
                        1, 11, 0,
                        2, 1, 0,
                        1, 2, 0,
                        0, 8, 65,
                        2, 7, 0,
                        1, 4, 0,
                        2, 4, 0,
                        0, 8, 64,
                        4, 10, 0,
                        3, 2, 0,
                        0, 8, 54,
                        1, 3, 0,
                        4, 12, 0,
                        1, 4, 0,
                        4, 11, 0,
                        3, 1, 0,
                        0, 1, 31,
                        4, 9, 0,
                        3, 18, 0,
                        2, 9, 0,
                        1, 2, 0,
                        0, 19, 22,
                        4, 9, 0,
                };

        place("T", 16, 13);
        place("1", 11, 5);

        for (int i = 0; i < placement.length / 3; i++) {
            int type = placement[3*i + 0];
            int r = placement[3*i + 1];
            int c = placement[3*i + 2];
            switch(type) {
                case 0:
                    place("", r, c);
                    break;
                case 1:
                    placeUp(r);
                    break;
                case 2:
                    placeRight(r);
                    break;
                case 3:
                    placeDown(r);
                    break;
                case 4:
                    placeLeft(r);
                    break;
            }
        }

    }
    public boolean place(String contents, int row, int col) {
        boolean isPlaced = false;
        if(tiles[row][col] == null) {
            tiles[row][col] = new Tile(contents, row, col);
            current = tiles[row][col];
            isPlaced = true;
        }
        else {
            tiles[row][col].setContents(contents);
            current = tiles[row][col];
        }
        int left = col - 1;
        int right = col + 1;
        int up = row - 1;
        int down = row + 1;
        if(up >= 0 && tiles[up][col] != null) {
            current.setUp(tiles[up][col]);
            tiles[up][col].setDown(current);
        }
        if(down < NUMROWS && tiles[down][col] != null) {
            current.setDown(tiles[down][col]);
            tiles[down][col].setUp(current);
        }
        if(left >= 0 && tiles[row][left] != null) {
            current.setLeft(tiles[row][left]);
            tiles[row][left].setRight(current);
        }
        if(right < NUMCOLS && tiles[row][right] != null) {
            current.setRight(tiles[row][right]);
            tiles[row][right].setLeft(current);
        }
        return isPlaced;
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
                int up = row - 1;
                int down = row + 1;
                if(up >= 0 && tiles[up][col] != null) {
                    current.setUp(tiles[up][col]);
                    tiles[up][col].setDown(current);
                }
                if(down < NUMROWS && tiles[down][col] != null) {
                    current.setDown(tiles[down][col]);
                    tiles[down][col].setUp(current);
                }
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
                int up = row - 1;
                int down = row + 1;
                if(up >= 0 && tiles[up][col] != null) {
                    current.setUp(tiles[up][col]);
                    tiles[up][col].setDown(current);
                }
                if(down < NUMROWS && tiles[down][col] != null) {
                    current.setDown(tiles[down][col]);
                    tiles[down][col].setUp(current);
                }
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
                int left = col - 1;
                int right = col + 1;
                if(left >= 0 && tiles[row][left] != null) {
                    current.setLeft(tiles[row][left]);
                    tiles[row][left].setRight(current);
                }
                if(right < NUMCOLS && tiles[row][right] != null) {
                    current.setRight(tiles[row][right]);
                    tiles[row][right].setLeft(current);
                }
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
                int left = col - 1;
                int right = col + 1;
                if(left >= 0 && tiles[row][left] != null) {
                    current.setLeft(tiles[row][left]);
                    tiles[row][left].setRight(current);
                }
                if(right < NUMCOLS && tiles[row][right] != null) {
                    current.setRight(tiles[row][right]);
                    tiles[row][right].setLeft(current);
                }
            }
        }
        return true;
    }

    // move function called by the network
    public boolean move(int id, int row, int col) {
        if(tiles[row][col] != null) {
            tiles[row][col].setContents("" + id);
            return true;
        }
        return false;
    }
    public boolean moveLeft() {
        return moveLeft(slimeID);
    }

    public boolean moveLeft(int id) {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        if(current.getLeft() != null) {
            current.setContents("");
            current = current.getLeft();
            current.setContents("" + id);
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
            if(current.getContents().equals("T")) {
                JOptionPane.showMessageDialog(null, "in tent");
                current = current.getDown();
                current.setContents("" + slimeID);
                return true;
            }
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
        xoffset = Math.min(GameClient.ImageWidth - GameClient.ScreenWidth, xoffset + 200);
        return true;
    }

    public boolean shiftLeft() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        xoffset = Math.max(0, xoffset - 200);
        return true;
    }

    public boolean shiftUp() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        yoffset = Math.max(0, yoffset - 200);
        return true;
    }

    public boolean shiftDown() {
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        yoffset = Math.min(GameClient.ImageHeight - GameClient.ScreenHeight, yoffset + 200);
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

