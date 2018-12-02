package game.Battles;


import game.DijkstraGrid;
import game.entities.IEntity;
import game.entities.slime.Slime;
import game.api.GameApi;
import game.entities.slimefactory.SlimeFactory;
import jig.Entity;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;

public class BattleGrid {

    public int gridHeight;
    public int gridWidth;
    public int screenHeight;
    public int screenWidth;
    public int gridPixleHeight;
    public int gridPixleWidth;

    public int tileSize;

    public int xBuffer;
    public int yBuffer;

    public GameApi gameApi;

    private int[][] gridState;

    private DijkstraGrid dijkstraGrid;
    private double[][] distanceGrid;

    public BattleGridTile[][] tileGrid;
    private BattleGridTile selectedTile;

    private Color shaded = new Color(0,0,0,50);

    public BattleGrid(final int screenHeight, final int screenWidth,
                      int yBuffer, GameApi gameApi, final int[][] map){

        this.gridState = map;
        this.gameApi = gameApi;

        this.screenHeight = screenHeight;
        this.yBuffer = yBuffer;
        this.gridPixleHeight = screenHeight - (2*yBuffer);
        this.gridHeight = map[0].length;

        this.tileSize = gridPixleHeight / gridHeight;

        this.screenWidth = screenWidth;
        this.gridWidth = map.length;
        this.xBuffer = ( screenWidth - ( gridWidth*tileSize )) / 2;
        this.gridPixleWidth = screenWidth - (2*xBuffer);

        dijkstraGrid = new DijkstraGrid(this.gridState);
        initBattleGrid(map);
    }

    public void setGameApi(GameApi gameApi) {
        this.gameApi = gameApi;
    }

    private void initBattleGrid(int[][] map){

        this.tileGrid = new BattleGridTile[this.gridWidth][this.gridHeight];
        this.dijkstraGrid = new DijkstraGrid(map);


        for(int i =0; i < this.gridWidth; i++){
            for(int j = 0; j < this.gridHeight; j++){

                Vector tilePosition = initTilePosition(i,j);

                BattleGridTile newTile = new BattleGridTile(tilePosition, i, j);

                this.tileGrid[i][j] = newTile;

                // TEMP
//                if( map[i][j] == 2 ){
////                    newTile.addOccupent(new Slime(1, 1));
//                    addOccupentTo(newTile, new SlimeFactory(1));
//                }
                // TEMP
            }
        }
    }

    private Vector initTilePosition(int i, int j){

        int x = (tileSize*i)+(tileSize/2)+xBuffer;
        int y = (tileSize*j)+(tileSize/2)+yBuffer;

        return new Vector(x,y);
    }

    public ArrayList<BattleGridTile> getAjacent(BattleGridTile tile){

        ArrayList<BattleGridTile> ajacent = new ArrayList<>();
        int x = tile.getxIndex();
        int y = tile.getyIndex();

        for (int xOffset = -1; xOffset <= 1; xOffset++){
            for (int yOffset = -1; yOffset <= 1; yOffset++){

                if( yOffset == 0 && xOffset == 0 ){
                    continue;
                }
                if( x+xOffset >= 0 && y+yOffset >= 0 &&
                        x+xOffset < gridWidth && y+yOffset < gridHeight ){

                    ajacent.add(getTile(x+xOffset, y+yOffset ));
                }
            }
        }
        return ajacent;
    }

    public void addOccupentTo(int x, int y, IEntity occupent){

        addOccupentTo(getTile(x, y), occupent);

    }

    private void addOccupentTo(BattleGridTile tile, IEntity occupent){
        if( occupent.getEntityType().equals("Slime") ) {
            tile.addOccupent(occupent);
        }
        else if ( occupent.getEntityType().equals("Factory") ){
            tile.addOccupent(occupent);
            ((SlimeFactory)occupent).setSpawnableTiles(getAjacent(tile));
        }
//        gridState[tile.getxIndex()][tile.getyIndex()] = 0;
    }

    private int getTileX(BattleGridTile tile){
        return (int)( tile.getX() - xBuffer ) / tileSize;
    }
    private int getTileY(BattleGridTile tile){
        return (int)( tile.getY() - yBuffer ) / tileSize;
    }

    private boolean withinGrid(Vector position){

        float x = position.getX();
        float y = position.getY();

        if( x > screenWidth-(xBuffer+(tileSize/6)) || x < xBuffer+(tileSize/6) ||
                y > screenHeight-(yBuffer+(tileSize/6)) || y < yBuffer+(tileSize/6) ){
            return false;
        }
        return true;
    }

    public BattleGridTile getTile(Vector position){

        int i = (int)(position.getX()-xBuffer) / tileSize;
        int j = (int)(position.getY()-yBuffer) / tileSize;

        if( i >= gridWidth || j >= gridHeight || i < 0 || j < 0 ){
            return null;
        }

        return getTile(i, j);
    }

    public BattleGridTile getTile(int x, int y){
        return this.tileGrid[x][y];
    }

    public void replaceTile(BattleGridTile newTile){
        newTile.setPosition( tileGrid[ newTile.getxIndex() ][ newTile.getyIndex() ].getPosition() );
        tileGrid[ newTile.getxIndex() ][ newTile.getyIndex() ] = newTile;

        if(newTile.hasOccupent()){
            ((Entity)newTile.getOccupent()).setPosition(newTile.getPosition());
        }
    }

    public void replaceOccupent(BattleGridTile newTile){

//        newTile.setPosition( tileGrid[ newTile.getxIndex() ][ newTile.getyIndex() ].getPosition() );
        BattleGridTile tileInGrid = getTile( newTile.getxIndex(), newTile.getyIndex() );
        tileInGrid.replaceOccupent(newTile.getOccupent());

    }

    public void setTile( int x, int y, BattleGridTile tile ){
        tileGrid[x][y] = tile;
    }

    public boolean tileSelected(){
        return this.selectedTile != null;
    }

    public void selectTile(Vector position){

        BattleGridTile tile = getTile(position);
        if(tile != null) {
            if( selectedTile == tile ){
                deselectTile();
            }
            else {
                selectTile(tile);
            }
        }

    }
    public void selectTile(BattleGridTile tile){

        int x = getTileX( tile );
        int y = getTileY( tile );

        if( this.tileSelected() ){
            if(inRange( this.selectedTile, x, y )) {
                moveOccupent(this.selectedTile, tile);
                deselectTile();
            }
        }
        else {
            if( tile.hasOccupent() && tile.getOccupent() instanceof Slime ) {
                distanceGrid = dijkstraGrid.getDistanceGrid( x, y );
                selectedTile = tile;
                shadeInRange();
            }
        }
    }

    public BattleGridTile getSelectedTile(){
        return selectedTile;
    }

    public void activateTile(Vector position){
        BattleGridTile tile = getTile(position);

        if( selectedTile != null && !selectedTile.hasOccupent() ){
            selectTile(tile);
        }
    }

    public void deselectTile(){
        selectedTile = null;
        shadeInRange();
    }

    private boolean inRange( BattleGridTile tile, int x, int y ){


        if(tile == null){
            return false;
        }
        if(!tile.hasOccupent()){
            throw new IllegalArgumentException("Error: Cannot call inRange on tile with no occupant");
        }
        if(!(tile.getOccupent() instanceof Slime)){
            throw new IllegalArgumentException("Error: Cannot call inRange on tile with non-slime occupant");
        }
        if( ((Slime) tile.getOccupent()).speed >= distanceGrid[x][y] ){
            return true;
        }
        return false;
    }

    private void shadeInRange(){

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                getTile(i, j).setShaded(inRange(selectedTile, i, j));
            }
        }
    }

    public void moveOccupent(BattleGridTile a, BattleGridTile b){

        Slime movingSlime;

        if(!a.hasOccupent()){
            throw new IllegalArgumentException("Cannot move occupant from tile with no occupant");
        }
        if(!(a.getOccupent() instanceof Slime)){
            System.err.println("Error: Only slimes can move");
            return;
        }
        if(!((Slime) a.getOccupent()).canMove()){
            return;
        }
        if(b.hasOccupent()){
            if(b.getOccupent() instanceof Slime && !a.getOccupent().equals(b.getOccupent()) &&
                    ((Slime) b.getOccupent()).clientID == ((Slime) a.getOccupent()).clientID){

                movingSlime = ((Slime) b.getOccupent()).combine((Slime) a.getOccupent());
            }
            else{
                System.err.println("Error: invalid move");
                return;
            }
        }
        else{
            movingSlime = (Slime) a.getOccupent();
        }
        BattleGridTile newTileA = new BattleGridTile( a.getPosition(), a.getxIndex(), a.getyIndex() );
        gameApi.createEntity(newTileA);

        BattleGridTile newTileB = new BattleGridTile( b.getPosition(), b.getxIndex(), b.getyIndex() );
        addOccupentTo(newTileB, movingSlime);
        gameApi.createEntity(newTileB);
    }

    public ArrayList<IEntity> getEntityList(){
        ArrayList<IEntity> entityList = new ArrayList<>();

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                BattleGridTile tile = tileGrid[i][j];
                if(tile.hasOccupent()){
                    entityList.add(tile.getOccupent());
                }
            }
        }
        return entityList;
    }

    private void drawGrid(Graphics g){

        Color c = g.getColor();
        g.setColor(Color.black);

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                BattleGridTile tile = tileGrid[i][j];

                g.drawRect(tile.getX()-tileSize/2, tile.getY()-tileSize/2,
                        tileSize, tileSize);
            }
        }
    }

    public void render(Graphics g){
        for(int i = 0; i < this.gridWidth; i++){
            for( int j = 0; j < this.gridHeight; j++){
                BattleGridTile tile = this.tileGrid[i][j];

                tile.render(g);
                if( tile.isShaded() ){
                    Color c = g.getColor();
                    g.setColor(shaded);
                    g.fillRect(tile.getX()-tileSize/2, tile.getY()-tileSize/2,
                            tileSize, tileSize);
                    g.setColor(c);
                }
                tile.renderOccupent(g);

            }
        }
        this.drawGrid(g);
    }
}
