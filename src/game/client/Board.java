package game.client;


import game.api.GameApi;
import game.entities.FightPopup;
import game.entities.IEntity;
import game.entities.building.TokenTents;
import game.entities.slime.Slime;
import game.entities.slimelord.SlimeLord;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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
    public Turn turn;
    GameClient gameClient;
    GameApi gameApi;
    Pathfinding pathfinding;

    List<TokenTents> tents;

    SlimeLord slimeLordOne;
    SlimeLord slimeLordTwo;
    SlimeLord slimeLordThree;
    SlimeLord slimeLordFour;
    FightPopup fightPopup;

    public LinkedList<SlimeLord> slimeLords = new LinkedList<>();

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
        slimeLordOne.id = "one";

        this.slimeLordTwo = new SlimeLord(1);
        slimeLordTwo.id = "two";

        this.slimeLordThree = new SlimeLord(2);
        slimeLordThree.id = "three";

        this.slimeLordFour = new SlimeLord(3);
        slimeLordFour.id = "four";

        turn = new Turn(gameApi, gameClient.myId);
        tents = new ArrayList<>();
        for(int i = 0; i < 12; i++){
            TokenTents tent = new TokenTents(4,0,0);
            tents.add(tent);
        }
        tents.get(0).setPosition(new Vector(8*16, 10*16));          // tent 1
        tents.get(1).setPosition(new Vector(1*16, 21*16));          // tent 2
        tents.get(2).setPosition(new Vector(17*16, 21*16));         // tent 3
        tents.get(3).setPosition(new Vector(17*16, 38*16));         // tent 4
        tents.get(4).setPosition(new Vector(26*16, 21*16));         // tent 5
        tents.get(5).setPosition(new Vector(49*16, 40*16));         // tent 6
        tents.get(6).setPosition(new Vector(61*16, 31*16));         // tent 7
        tents.get(7).setPosition(new Vector(77*16, 23*16));         // tent 8
        tents.get(8).setPosition(new Vector(52*16, 16*16));         // tent 9
        tents.get(9).setPosition(new Vector(71*16, 9*16));         // tent 10
        tents.get(10).setPosition(new Vector(60*16, 4));            // tent 11
        tents.get(11).setPosition(new Vector(26*16, 4));           // tent 12

//        updateSlimelord();

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

        moveSlimelordTo(slimeLordOne, 10, 5);
        slimeLords.add(slimeLordOne);

        if (gameClient.players.length > 1) {
            moveSlimelordTo(slimeLordTwo, 4, 75);
            slimeLords.add(slimeLordTwo);
        }

        if (gameClient.players.length > 2) {
            moveSlimelordTo(slimeLordThree, 40, 81);
            slimeLords.add(slimeLordThree);
        }

        if (gameClient.players.length > 3) {
            moveSlimelordTo(slimeLordFour, 40, 5);
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

    public void onDeleteEntity(String entityId) { }

    void onCreateSlimeLord(SlimeLord slimeLord) {
        SlimeLord selected = null;

        for (SlimeLord lord : slimeLords) {
            if (lord.id.equals(slimeLord.id)) selected = lord;
        }

        int tileX = (int)slimeLord.tilePosition.getX();
        int tileY = (int)slimeLord.tilePosition.getY();

        if (selected != null) {
            tiles[(int)selected.tilePosition.getX()][(int)selected.tilePosition.getY()].heldSlimeLord = null;
            selected.specialSlimes = slimeLord.specialSlimes;
            selected.abilities = slimeLord.abilities;
            moveSlimelordTo(selected, tileX, tileY);
        } else {
            moveSlimelordTo(slimeLord, tileX, tileY);
            slimeLords.add(slimeLord);
        }
    }

    void moveSlimelordTo(SlimeLord lord, int row, int col) {
        if (tiles[row][col] == null) return;

        tiles[row][col].heldSlimeLord = lord;
        lord.setPosition(tiles[row][col].getPosition());
        lord.tilePosition = new Vector(row, col);
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

        if (turn.turnID == gameClient.myId) {
            g.drawString("My turn!", 500, 470);
        } else {
            g.drawString("Player " + turn.turnID + "'s turn", 500, 470);
        }

        if (fightPopup != null) fightPopup.render(g);
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
                        1, 4, 0,        // tent
                        0, 20, 11,
                        3, 14, 0,
                        4, 6, 0,
                        3, 5, 0,
                        0, 34, 6,
                        1, 6, 0,        // tent
                        0, 34, 12,
                        2, 3, 0,
                        3, 14, 0,
                        2, 7, 0,
                        1, 3, 0,        // tent
                        0, 46, 23,
                        2, 5, 0,
                        1, 15, 0,
                        0, 30, 12,
                        2, 10, 0,
                        1, 3, 0,        // tent
                        0, 30, 22,
                        2, 9, 0,
                        3, 2, 0,
                        0, 30, 31,
                        1, 3, 0,
                        0, 30, 31,
                        2, 6, 0,
                        3, 18, 0,
                        2, 17, 0,       // tent 6
                        0, 47, 45,
                        1, 7, 0,
                        2, 9, 0,
                        1, 1, 0,
                        0, 40, 54,
                        2, 6, 0,
                        1, 14, 0,   // up
                        0, 40, 60,
                        2, 6, 0,
                        1, 2, 0,
                        0, 40, 67,
                        2, 6, 0,
                        1, 7, 0,
                        2, 8, 0,
                        3, 6, 0,
                        0, 33, 81,
                        2, 1, 0,
                        1, 3, 0,        // tent 8
                        0, 33, 77,
                        1, 7, 0,
                        4, 20, 0,
                        1, 3, 0,    // tent 9
                        0, 26, 70,
                        1, 7, 0,
                        2, 6, 0,
                        1, 3, 0,        // tent 10
                        0, 19, 70,
                        4, 6, 0,
                        1, 11, 0,
                        2, 1, 0,
                        1, 2, 0,        // tent 11
                        0, 8, 65,
                        2, 7, 0,
                        1, 4, 0,
                        2, 3, 0,
                        0, 8, 64,
                        4, 10, 0,
                        3, 2, 0,
                        0, 8, 54,
                        4, 23, 0,
                        1, 1, 0,
                        0, 8, 31,
                        4, 9, 0,
                        3, 11, 0,
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

        place("T:4:0", 16, 13);            // tent 1
        place("T:4:1", 27, 6);             // tent 2
        place("T:4:2", 28, 22);            // tent 3
        place("T:4:3", 45, 22);            // tent 4
        place("T:4:4", 28, 31);            // tent 5
        place("T:4:5", 47, 54);            // tent 6
        place("T:4:6", 38, 66);            // tent 7
        place("T:4:7", 30, 82);            // tent 8
        place("T:4:8", 23, 57);            // tent 9
        place("T:4:9", 16, 76);            // tent 10
        place("T:4:10", 7, 65);            // tent 11
        place("T:4:11", 7, 31);            // tent 12

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

        if (gc.myId == turn.turnID) {
            System.out.println("my turn");
            for (SlimeLord lord : slimeLords) {
                lord.hasMoved = false;
            }

            for (TokenTents tent : tents) {
                if (tent.owner == gameClient.myId) {
                    gameClient.setTokens(gameClient.tokens + tent.TOKEN_AMOUNT);
                }
            }
        }

    }

    public boolean click(int x, int y){
        if(x >= 0 && x <= 1392 && y >= 0 && y <= 800 && turn.isMyMove()){
            int row = (int)(y + yoffset)/16;
            int col = (int)(x + xoffset)/16;

            Tile tile = tiles[row][col];

            if (tile == null) return false;

            if (currentSlimelord != null) { // where i have selected a slime lord ready to move
                if (tile.isHighlighted && tile.heldSlimeLord == null) {
                    dehighlightMovement((int)currentSlimelord.tilePosition.getX(), (int)currentSlimelord.tilePosition.getY(), currentSlimelord.totalMovement);
                    currentSlimelord.tilePosition = new Vector(row, col);
                    currentSlimelord.hasMoved = true;
                    gameApi.createEntity(currentSlimelord);

                    checkForTent(currentSlimelord);
                    checkForBattle(currentSlimelord);

                    currentSlimelord = null;
                }
            } else { // select a slime lord if available
                currentSlimelord = (tile.heldSlimeLord != null && !tile.heldSlimeLord.hasMoved) ? tile.heldSlimeLord : null;

                if (currentSlimelord != null && currentSlimelord.clientID != gameClient.myId) currentSlimelord = null;

                if (currentSlimelord != null) {
                    highlightMovement((int)currentSlimelord.tilePosition.getX(), (int)currentSlimelord.tilePosition.getY(), currentSlimelord.remainingMovement);
                }
            }
        }

        return false;
    }

    void checkForBattle(SlimeLord lord) {
        int x = (int)lord.tilePosition.getX();
        int y = (int)lord.tilePosition.getY();

        SlimeLord opponent = null;

        if (x > 0) {
            if (tiles[x - 1][y] != null && tiles[x - 1][y].heldSlimeLord != null) opponent = tiles[x - 1][y].heldSlimeLord;
        }

        if (x < NUMROWS) {
            if (tiles[x + 1][y] != null && tiles[x + 1][y].heldSlimeLord != null) opponent = tiles[x + 1][y].heldSlimeLord;
        }

        if (y > 0) {
            if (tiles[x][y - 1] != null && tiles[x][y - 1].heldSlimeLord != null) opponent = tiles[x][y - 1].heldSlimeLord;
        }

        if (y < NUMCOLS) {
            if (tiles[x][y + 1] != null && tiles[x][y + 1].heldSlimeLord != null) opponent = tiles[x][y + 1].heldSlimeLord;
        }

        if (opponent != null) {
            fightPopup = new FightPopup(new Vector(500, 250), lord, opponent);
        }
    }

    void checkForTent(SlimeLord lord) {
        int x = (int)lord.tilePosition.getX();
        int y = (int)lord.tilePosition.getY();

        if (tiles[x][y] != null) isTent(tiles[x][y], lord.clientID);

        if (x > 0) {
            if (tiles[x - 1][y] != null) isTent(tiles[x - 1][y], lord.clientID);
        }

        if (x < NUMROWS) {
            if (tiles[x + 1][y] != null) isTent(tiles[x + 1][y], lord.clientID);
        }

        if (y > 0) {
            if (tiles[x][y - 1] != null) isTent(tiles[x][y - 1], lord.clientID);
        }

        if (y < NUMCOLS) {
            if (tiles[x][y + 1] != null) isTent(tiles[x][y + 1], lord.clientID);
        }
    }

    public void highlightMovement(int row, int col, int rmvmt) {
        if (rmvmt <= 0 ||
                col <= 0 || col >= NUMCOLS ||
                row <= 0 || row >= NUMROWS ||
                currentSlimelord == null ||
                tiles[row][col] == null ||
                tiles[row][col].visited) return;

        tiles[row][col].isHighlighted = true;
        tiles[row][col].visited = true;

        highlightMovement(row + 1, col + 1, rmvmt - 1);
        highlightMovement(row + 1, col, rmvmt - 1);
        highlightMovement(row + 1, col - 1, rmvmt - 1);

        highlightMovement(row, col + 1, rmvmt - 1);
        highlightMovement(row, col, rmvmt - 1);
        highlightMovement(row, col - 1, rmvmt - 1);

        highlightMovement(row - 1, col + 1, rmvmt - 1);
        highlightMovement(row - 1, col, rmvmt - 1);
        highlightMovement(row - 1, col - 1, rmvmt - 1);

        tiles[row][col].visited = false;
    }

    public void dehighlightMovement(int row, int col, int rmvmt) {
        if (rmvmt <= 0 ||
                col <= 0 || col >= NUMCOLS ||
                row <= 0 || row >= NUMROWS ||
                tiles[row][col] == null ||
                tiles[row][col].visited) return;

        tiles[row][col].visited = true;
        tiles[row][col].isHighlighted = false;

        dehighlightMovement(row + 1, col + 1, rmvmt - 1);
        dehighlightMovement(row + 1, col, rmvmt - 1);
        dehighlightMovement(row + 1, col - 1, rmvmt - 1);

        dehighlightMovement(row, col + 1, rmvmt - 1);
        dehighlightMovement(row, col, rmvmt - 1);
        dehighlightMovement(row, col - 1, rmvmt - 1);

        dehighlightMovement(row - 1, col + 1, rmvmt - 1);
        dehighlightMovement(row - 1, col, rmvmt - 1);
        dehighlightMovement(row - 1, col - 1, rmvmt - 1);

        tiles[row][col].visited = false;
    }

    private boolean isTent(Tile tile, int clientId) {
        boolean isTent = tile.getContents().startsWith("T");

        if(isTent){
            String[] split = tile.getContents().split(":");
            int owner = Integer.parseInt(split[1]);
            int id = Integer.parseInt(split[2]);
            if(owner == 4){
                tile.setContents("T:" + clientId + ":" + id);
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

    public void update(int delta, Input input) {
        countdown = countdown - delta;
        if(countdown < 0) {
            countdown = KEYBOARD_COUNTDOWN;
            acceptKeyboard = true;
        }

        if (fightPopup != null) {
            fightPopup.update(input);
            
            if (fightPopup.IsFighting) {
                gameApi.startBattle(fightPopup.one, fightPopup.two);
                fightPopup = null;
            } else if (fightPopup.IsCoward) {
                fightPopup = null;
            }
        }
    }
}

