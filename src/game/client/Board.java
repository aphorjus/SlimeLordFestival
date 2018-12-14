package game.client;


import game.api.GameApi;
import game.entities.IEntity;
import game.entities.building.TokenTents;
import game.entities.slime.Slime;
import game.entities.slimelord.SlimeLord;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import jig.ResourceManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
    public static final String OVERWORLD_RSC = "game/client/resource/overworld.png";
    public static final String TILE_RSC = "game/client/resource/tile.png";
    public static final String HIGHLIGHTED_TILE_RSC = "game/client/resource/highlight.png";

    // Tents
    public static final String OPEN_TOKENTENT = "game/client/resource/tokentent.png";    // unconquered
    public static final String GREEN_TOKENTENT = "game/client/resource/green-tokentent.png";
    public static final String RED_TOKENTENT = "game/client/resource/red-tokentent.png";
    public static final String YELLOW_TOKENTENT = "game/client/resource/orange-tokentent.png";
    public static final String BLUE_TOKENTENT = "game/client/resource/blue-tokentent.png";

    // Arenas
    public static final String OPEN_ARENA = "game/client/resource/arena.png";           // unconquered
    public static final String GREEN_ARENA= "game/client/resource/green-arena.png";
    public static final String RED_ARENA = "game/client/resource/red-arena.png";
    public static final String YELLOW_ARENA = "game/client/resource/orange-arena.png";
    public static final String BLUE_ARENA= "game/client/resource/blue-arena.png";

    public static int NUMROWS = 50;
    public static int NUMCOLS = 200;
    public static int KEYBOARD_COUNTDOWN = 100;
    private int countdown = KEYBOARD_COUNTDOWN;
    private boolean acceptKeyboard = false;
    private final Tile[][] tiles;
    private Tile current;
    private float xoffset = 0;
    private float yoffset = 0;
    private int slimeID = 0;
    public Turn turn;
    GameClient gameClient;
    GameApi gameApi;
    Pathfinding pathfinding;

    List<TokenTents> tents;

    SlimeLord slimeLordOne;
    SlimeLord slimeLordTwo;
    SlimeLord slimeLordThree;
    SlimeLord slimeLordFour;

    LinkedList<SlimeLord> slimeLords = new LinkedList<>();

    public SlimeLord currentSlimelord;


    public Board() {
        tiles = new Tile[NUMROWS][NUMCOLS];
        pathfinding = new Pathfinding(tiles);
        // turn = new Turn(0);
    }

    public void setUp(GameApi gameApi, GameClient gameClient) {
        this.gameApi = gameApi;
        this.gameClient = gameClient;
        this.slimeLordOne = new SlimeLord(0);
        this.slimeLordTwo = new SlimeLord(1);
        this.slimeLordThree = new SlimeLord(2);
        this.slimeLordFour = new SlimeLord(3);

        turn = new Turn(gameApi, gameClient.myId);
        tents = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            TokenTents tent = new TokenTents(4,0,0);
            tents.add(tent);
        }
        tents.get(0).setPosition(new Vector(8*16, 10*16));           // tent

        /*
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

         */

        slimeLords.add(slimeLordOne);
        moveSlimelordTo(slimeLordOne, 5, 10);

        if (gameClient.players.length > 1) {
            moveSlimelordTo(slimeLordTwo, 76, 4);
            slimeLords.add(slimeLordTwo);
        }

        if (gameClient.players.length > 2) {
            moveSlimelordTo(slimeLordThree, 81, 39);
            slimeLords.add(slimeLordThree);

        }

        if (gameClient.players.length > 3) {
            moveSlimelordTo(slimeLordFour, 5, 39);
            slimeLords.add(slimeLordFour);
        }
    }

    public void onCreateEntity(IEntity entity) {
        switch (entity.getEntityType()) {
            case "slime_lord":
                onCreateSlimeLord((SlimeLord) entity);
                break;
        }
    }

    public void onDeleteEntity(String entityId) {
    }

    void onCreateSlimeLord(SlimeLord slimeLord) {
        SlimeLord selected = null;

        for (SlimeLord lord : slimeLords) {
            if (lord.id.equals(slimeLord.id)) selected = lord;
        }

        int tileX = (int)slimeLord.tilePosition.getX();
        int tileY = (int)slimeLord.tilePosition.getY();

        if (selected != null) {
            tiles[(int)selected.tilePosition.getY()][(int)selected.tilePosition.getX()].heldSlimeLord = null;
            moveSlimelordTo(selected, tileX, tileY);
        } else {
            moveSlimelordTo(slimeLord, tileX, tileY);
            slimeLords.add(slimeLord);
        }
    }

    public void moveSlimelordTo(SlimeLord lord, int row, int col) {
        tiles[col][row].heldSlimeLord = lord;
        lord.setPosition(tiles[col][row].getPosition());
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
                    tiles[row][col].render(g, new Vector(xoffset, yoffset));
                }
            }
        }

        for(TokenTents tent: tents){
            tent.setCameraOffset(new Vector(xoffset, yoffset));
            tent.positionForCamera();
            tent.render(g);
            tent.positionToOrigin();
        }
    }

    // setting up tiles
    public void initialize() {
        ResourceManager.loadImage(OVERWORLD_RSC);
        ResourceManager.loadImage(TILE_RSC);
        ResourceManager.loadImage(HIGHLIGHTED_TILE_RSC);

        ResourceManager.loadImage(OPEN_TOKENTENT);
        ResourceManager.loadImage(GREEN_TOKENTENT);
        ResourceManager.loadImage(RED_TOKENTENT);
        ResourceManager.loadImage(YELLOW_TOKENTENT);
        ResourceManager.loadImage(BLUE_TOKENTENT);

        ResourceManager.loadImage(OPEN_ARENA);
        ResourceManager.loadImage(GREEN_ARENA);
        ResourceManager.loadImage(RED_ARENA);
        ResourceManager.loadImage(YELLOW_ARENA);
        ResourceManager.loadImage(BLUE_ARENA);

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
                        2, 4, 0, //
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
        place("T:4:0", 16, 13);           // tent
        place("T:4:1", 3, 31);            // tent
        // place("T:0", 16, 31);                          // shop

        // RED SLIME AREA
        place("T:4:2", 27, 6);             // tent
        place("T:4:3", 44, 18);            // tent
        place("T:4:4", 34, 22);            // tent
        place("T:4:5", 27, 31);            // tent
       // place("T:0", 33, 31);                            // shop

        // ORANGE SLIME AREA
        place("T:4:6", 47, 48);           // path leading right tent DOES NOT WORK
        //  place("T:0", 38, 54);                         // shop
        place("T:4:7", 37, 66);           // tent
        place("T:4:8", 29, 82);           // tent

        // GREEN SLIME AREA
        place("T:4:9", 15, 76);           // tent
        place("T:4:10", 5, 65);            // tent
        place("T:4:11", 22, 57);           // tent
        // place("T:0", 11, 54);                          // shop

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

    public void endTurn(GameClient gc){
        turn.turnHasEnded(gc);
        showCurrentHighlightedPaths();

        if (gc.myId == turn.turnID) {
            for (SlimeLord lord : slimeLords) {
                if (lord.clientID == gameClient.myId) {
                    lord.remainingMovement = lord.totalMovement;
                }
            }

            for (TokenTents tent : tents) {
                if (tent.owner == gameClient.myId) {
                    gameClient.setTokens(gameClient.tokens + tent.TOKEN_AMOUNT);
                }
            }
        }
    }

    public void move(int id, float xpos, float ypos) {
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

    public boolean click(int x, int y){
        if(x >= 0 && x <= 1392 && y >= 0 && y <= 800 && turn.isMyMove()){
            int row = y/16;
            int col = x/16;

            Tile tile = tiles[row][col];

            if (tile == null) return false;

            if (currentSlimelord != null) { // where i have selected a slime lord ready to move
                dehighlightMovement((int)currentSlimelord.tilePosition.getY(), (int)currentSlimelord.tilePosition.getX(), currentSlimelord.totalMovement);
                currentSlimelord.tilePosition = new Vector(col, row);
                gameApi.createEntity(currentSlimelord);
                currentSlimelord = null;
            } else { // select a slime lord if available
                currentSlimelord = tile.heldSlimeLord;

                if (currentSlimelord != null) {
                    highlightMovement((int)currentSlimelord.tilePosition.getY(), (int)currentSlimelord.tilePosition.getX(), currentSlimelord.remainingMovement);
                }
            }
        }

        return false;
    }

    public void highlightMovement(int col, int row, int rmvmt) {
        if (rmvmt <= 0 ||
                col <= 0 || col >= NUMCOLS ||
                row <= 0 || row >= NUMROWS ||
                currentSlimelord == null ||
                tiles[col][row] == null ||
                tiles[col][row].visited) return;

        tiles[col][row].isHighlighted = true;

        highlightMovement(col + 1, row + 1, rmvmt - 1);
        highlightMovement(col + 1, row, rmvmt - 1);
        highlightMovement(col + 1, row - 1, rmvmt - 1);

        highlightMovement(col, row + 1, rmvmt - 1);
        highlightMovement(col, row, rmvmt - 1);
        highlightMovement(col, row - 1, rmvmt - 1);

        highlightMovement(col - 1, row + 1, rmvmt - 1);
        highlightMovement(col - 1, row, rmvmt - 1);
        highlightMovement(col - 1, row - 1, rmvmt - 1);

        tiles[col][row].visited = false;
    }

    public void dehighlightMovement(int col, int row, int rmvmt) {
        if (rmvmt <= 0 ||
                col <= 0 || col >= NUMCOLS ||
                row <= 0 || row >= NUMROWS ||
                tiles[col][row] == null ||
                tiles[col][row].visited) return;

        tiles[col][row].visited = true;
        tiles[col][row].isHighlighted = false;

        dehighlightMovement(col + 1, row + 1, rmvmt - 1);
        dehighlightMovement(col + 1, row, rmvmt - 1);
        dehighlightMovement(col + 1, row - 1, rmvmt - 1);

        dehighlightMovement(col, row + 1, rmvmt - 1);
        dehighlightMovement(col, row, rmvmt - 1);
        dehighlightMovement(col, row - 1, rmvmt - 1);

        dehighlightMovement(col - 1, row + 1, rmvmt - 1);
        dehighlightMovement(col - 1, row, rmvmt - 1);
        dehighlightMovement(col - 1, row - 1, rmvmt - 1);

        tiles[col][row].visited = false;
    }

    public void showCurrentHighlightedPaths(){
        int row = current.getRow();
        int col = current.getCol();
        if(row < NUMROWS && col < NUMCOLS && tiles[row][col] != null){
            showHighlighted(row, col);
        }
    }

    public void showHighlightedPaths(int x, int y){
        if(x >= 0 && x <= 1392 && y >= 0 && y <= 800 && turn.isMyMove()){
//            int row = y/16;
//            int col = x/16;
            int row = current.getRow();
            int col = current.getCol();
            if(row < NUMROWS && col < NUMCOLS && tiles[row][col] != null){
                showHighlighted(row, col);
            }
        }
    }
    private void showHighlighted(int row, int col){
        reset();
        Tile tile = tiles[row][col];
        List<String> paths = pathfinding.showAllPaths(tile,Turn.NUM_MOVES - turn.getMove());
        Tile last = tile;
        tile.isHighlighted = true;
        for(String path: paths){
            for(char c: path.toCharArray()){
                if(c == 'd'){
                    tile = tile.down;
                } else if(c == 'u'){
                    tile = tile.up;
                } else if(c == 'l'){
                    tile = tile.left;
                } else if(c == 'r'){
                    tile = tile.right;
                }
                tile.isHighlighted = true;
            }
            tile = last;
        }
    }

    public void reset() {
        if(tiles == null){
            return;
        }
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; tiles[row] != null && col < tiles[row].length; col++) {
                if(tiles[row][col] != null){
                    tiles[row][col].isHighlighted = false;
                }
            }
        }
    }

    public boolean isBattle() {
        if(gameClient.myId == 0){
            return currentSlimelord.getX() == slimeLordTwo.getX() && currentSlimelord.getY() == slimeLordTwo.getY() ||
                    currentSlimelord.getX() == slimeLordThree.getX() && currentSlimelord.getY() == slimeLordThree.getY() ||
                    currentSlimelord.getX() == slimeLordFour.getX() && currentSlimelord.getY() == slimeLordFour.getY();
        }
        if(gameClient.myId == 1){
            return currentSlimelord.getX() == slimeLordOne.getX() && currentSlimelord.getY() == slimeLordOne.getY() ||
                    currentSlimelord.getX() == slimeLordThree.getX() && currentSlimelord.getY() == slimeLordThree.getY() ||
                    currentSlimelord.getX() == slimeLordFour.getX() && currentSlimelord.getY() == slimeLordFour.getY();
        }
        if(gameClient.myId == 2){
            return currentSlimelord.getX() == slimeLordOne.getX() && currentSlimelord.getY() == slimeLordOne.getY() ||
                    currentSlimelord.getX() == slimeLordTwo.getX() && currentSlimelord.getY() == slimeLordTwo.getY() ||
                    currentSlimelord.getX() == slimeLordFour.getX() && currentSlimelord.getY() == slimeLordFour.getY();
        }
        if(gameClient.myId == 3){
            return currentSlimelord.getX() == slimeLordOne.getX() && currentSlimelord.getY() == slimeLordOne.getY() ||
                    currentSlimelord.getX() == slimeLordTwo.getX() && currentSlimelord.getY() == slimeLordTwo.getY() ||
                    currentSlimelord.getX() == slimeLordThree.getX() && currentSlimelord.getY() == slimeLordThree.getY();
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
                if(isBattle()){
                    gameApi.setGameState(GameApi.SetGameStateBattle);
                }
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(currentSlimelord.clientID);
            currentSlimelord.moveLeft();
            gameApi.createEntity(currentSlimelord);
            showCurrentHighlightedPaths();
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
                if(isBattle()){
                    gameApi.setGameState(GameApi.SetGameStateBattle);
                }
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(currentSlimelord.clientID);
            currentSlimelord.moveRight();
            gameApi.createEntity(currentSlimelord);
            showCurrentHighlightedPaths();
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
                if(isBattle()){
                    gameApi.setGameState(GameApi.SetGameStateBattle);
                }
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(currentSlimelord.clientID);
            currentSlimelord.moveUp();
            gameApi.createEntity(currentSlimelord);
            showCurrentHighlightedPaths();
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
                if(isBattle()){
                    gameApi.setGameState(GameApi.SetGameStateBattle);
                }
                current.setContents("" + slimeID);
            }
            gameApi.deleteEntity(gameClient.myId);
            currentSlimelord.moveDown();
            gameApi.createEntity(currentSlimelord);
            showCurrentHighlightedPaths();
            return true;
        }
        return false;

    }

    private boolean isTent() {
        boolean isTent = current.getContents().startsWith("T");

        if(isTent){
            String[] split = current.getContents().split(":");
            int owner = Integer.parseInt(split[1]);
            int id = Integer.parseInt(split[2]);
            if(owner == 4){
                current.setContents("T:" + slimeID + ":" + id);
                tents.get(id).owner = gameClient.myId;
            } else if(owner == gameClient.myId){
                //JOptionPane.showMessageDialog(null, "You own this tent.");
            } else {
                //JOptionPane.showMessageDialog(null, "tent is already owned by " + id);
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

