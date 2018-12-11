package game.client;

import javax.swing.JOptionPane;


import game.api.GameApi;
import game.entities.slimelord.SlimeLord;
import jig.Vector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import jig.ResourceManager;

public class Board {
    public static final String OVERWORLD_RSC = "game/client/resource/overworld.png";
    public static final String TILE_RSC = "game/client/resource/tile.png";
    public static final String BLUE_SLIMELORD_RSC = "game/client/resource/blue-slimelord.png";
    public static final String GREEN_SLIMELORD_RSC = "game/client/resource/yellow-slimelord.png";
    public static final String ORANGE_SLIMELORD_RSC = "game/client/resource/orange-slimelord.png";
    public static final String RED_SLIMELORD_RSC = "game/client/resource/red-slimelord.png";
    public static final String TOKENTENT_RSC = "game/client/resource/tokentent.png";    // unconquered

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
    private int slimeID = 0;
    private Turn turn;
    GameClient gameClient;
    GameApi gameApi;
    Pathfinding pathfinding;

    SlimeLord slimeLordOne;
    SlimeLord slimeLordTwo;
    SlimeLord slimeLordThree;
    SlimeLord slimeLordFour;

    public SlimeLord currentSlimelord;


    public Board() {
        pathfinding = new Pathfinding();
        tiles = new Tile[NUMROWS][NUMCOLS];
        // turn = new Turn(0);
    }

    public void setUp(GameApi gameApi, GameClient gameClient) {
        this.gameApi = gameApi;
        this.gameClient = gameClient;
        this.slimeLordOne = new SlimeLord(0);
        this.slimeLordTwo = new SlimeLord(1);
        this.slimeLordThree = new SlimeLord(2);
        this.slimeLordFour = new SlimeLord(3);
        //gameApi.createEntity(slimeLordOne);
        //gameApi.createEntity(slimeLordTwo);
        //gameApi.createEntity(slimeLordThree);
        //gameApi.createEntity(slimeLordFour);

        turn = new Turn(gameApi, gameClient.myId);

        slimeLordOne.setPosition(new Vector(5*16,10*16));       //  blue
        slimeLordTwo.setPosition(new Vector(76*16,4*16));       // green
        slimeLordThree.setPosition(new Vector(81*16,39*16));    // orange
        slimeLordFour.setPosition(new Vector(5*16,39*16));      // red

        updateSlimelord();
        switch(gameClient.myId) {
            case 0:
                place("", 10,5);
                break;
            case 1:
                place("", 4,76);
                break;
            case 2:
                place("", 39,81);
                break;
            case 3:
                place("", 39,5);
                break;
        }
    }

    public void updateSlimelord() {
        switch(gameClient.myId){
            case 0: currentSlimelord = slimeLordOne;
                break;
            case 1: currentSlimelord = slimeLordTwo;
                break;
            case 2: currentSlimelord = slimeLordThree;
                break;
            case 3: currentSlimelord = slimeLordFour;
                break;
        }
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
        slimeLordOne.setOffsets(xoffset, yoffset);
        slimeLordOne.render(g);
        slimeLordTwo.setOffsets(xoffset, yoffset);
        slimeLordTwo.render(g);
        slimeLordThree.setOffsets(xoffset, yoffset);
        slimeLordThree.render(g);
        slimeLordFour.setOffsets(xoffset, yoffset);
        slimeLordFour.render(g);
    }

    // setting up tiles
    public void initialize() {
        ResourceManager.loadImage(OVERWORLD_RSC);
        ResourceManager.loadImage(TILE_RSC);
        ResourceManager.loadImage(BLUE_SLIMELORD_RSC);
        ResourceManager.loadImage(GREEN_SLIMELORD_RSC);
        ResourceManager.loadImage(ORANGE_SLIMELORD_RSC);
        ResourceManager.loadImage(RED_SLIMELORD_RSC);
        ResourceManager.loadImage(TOKENTENT_RSC);

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
                        1, 4, 0,
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
        // BLUE SLIME AREA
        place("T:0", 16, 13);           // tent
        place("T:0", 3, 31);            // tent
        // place("T:0", 16, 31);                          // shop

        // RED SLIME AREA
        place("T:0", 27, 6);             // tent
        place("T:0", 44, 18);            // tent
        place("T:0", 34, 22);            // tent
        place("T:0", 27, 31);            // tent
       // place("T:0", 33, 31);                            // shop

        // ORANGE SLIME AREA
        place("T:0", 47, 48);           // path leading right tent DOES NOT WORK
        //  place("T:0", 38, 54);                         // shop
        place("T:0", 37, 66);           // tent
        place("T:0", 29, 82);           // tent

        // GREEN SLIME AREA
        place("T:0", 15, 76);           // tent
        place("T:0", 5, 65);            // tent
        place("T:0", 22, 57);           // tent
        // place("T:0", 11, 54);                          // shop

       // place("2", 4, 76);  // green
        //slimeLordTwo.setX(current.getX());
        //slimeLordTwo.setY(current.getY());

        //place("3", 39, 81);  // orange
       // slimeLordThree.setX(current.getX());
       // slimeLordThree.setY(current.getY());

       // place("4", 39, 5);  // red
       // slimeLordFour.setX(current.getX());
       // slimeLordFour.setY(current.getY());

        //place("1", 10, 5);  // blue
       // slimeLordOne.setX(current.getX());
       // slimeLordOne.setY(current.getY());
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

    public boolean isMyTurn() {
        if(turn.isMyMove()){
            return turn.makeMove();
        }
        return false;
    }

    public void endTurn(){
        turn.turnHasEnded();
    }

    public void move(int id, float xpos, float ypos) {
        // System.out.println(gameClient.myId + " " + id + " " + xpos + " " + ypos);
        if(gameClient.myId != id && id == 0){
            slimeLordOne.moveTo(xpos, ypos);
        }
        if(gameClient.myId != id && id == 1){
            slimeLordTwo.moveTo(xpos, ypos);
        }
        if(gameClient.myId != id && id == 2){
            slimeLordThree.moveTo(xpos, ypos);
        }
        if(gameClient.myId != id && id == 3){
            slimeLordFour.moveTo(xpos, ypos);
        }
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
        if(!acceptKeyboard) {
            return false;
        }
        acceptKeyboard = false;
        if(current.getLeft() != null) {
            if(!isMyTurn()){
                return false;
            }
            current.setContents("");
            current = current.getLeft();
            if(isTent()) {
                current = current.getRight();
            } else {
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(currentSlimelord.clientID);
            currentSlimelord.moveLeft();
            gameApi.createEntity(currentSlimelord);
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
            if(!isMyTurn()){
                return false;
            }
            current.setContents("");
            current = current.getRight();
            if(isTent()) {
                current = current.getLeft();
            } else {
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(currentSlimelord.clientID);
            currentSlimelord.moveRight();
            gameApi.createEntity(currentSlimelord);
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
            if(!isMyTurn()){
                return false;
            }
            current.setContents("");
            current = current.getUp();
            if(isTent()) {
                current = current.getDown();
            } else {
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(currentSlimelord.clientID);
            currentSlimelord.moveUp();
            gameApi.createEntity(currentSlimelord);
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
            if(!isMyTurn()){
                return false;
            }
            current.setContents("");
            current = current.getDown();
            if(isTent()) {
                current = current.getUp();
            } else {
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(gameClient.myId);
            currentSlimelord.moveDown();
            gameApi.createEntity(currentSlimelord);
            pathfinding.showAllPaths(tiles, current);
            return true;
        }
        return false;

    }

    private boolean isTent() {
        boolean isTent = current.getContents().startsWith("T");
        if(isTent){
            String[] split = current.getContents().split(":");
            int id = Integer.parseInt(split[1]);
            if(id == 0){
                JOptionPane.showMessageDialog(null, "You now own this tent.");
                current.setContents("T:" + slimeID);
            } else if(id != slimeID){
                JOptionPane.showMessageDialog(null, "tent is already owned by " + id);
            } else {
                JOptionPane.showMessageDialog(null, "You own this tent.");

            }
            acceptKeyboard = false;
        }

        return isTent;
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

