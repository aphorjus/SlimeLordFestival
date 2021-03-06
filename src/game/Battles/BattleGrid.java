package game.Battles;

import game.DijkstraGrid;
import game.IntVector;
import game.client.GameClient;
import game.entities.IEntity;
import game.entities.slime.Slime;
import game.api.GameApi;
import game.entities.slimefactory.SlimeFactory;
import game.entities.slimelord.BattleAbility;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.lwjgl.Sys;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.LinkedList;

public class BattleGrid {


    public static final int MOVMENT_MODE = 1;
    public static final int ATTACK_MODE = 2;
    public static final int ABILITY_MODE = 3;
    public int mode = MOVMENT_MODE;

    private int winner = -1;

    public BattleAbility ability = new BattleAbility();

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
    private BattleGridTile currentlySelectedTile;
//    private BattleAbility  currentlySelectedAblity = new BattleAbility();

    private Color shaded = new Color(0,0,0,50);
    private Color highlight = new Color(0,0,0,75);
    AnimatedBattleBackground background = new AnimatedBattleBackground();

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
//        this.ability = new BattleAbility(gameApi);
    }

    public void setMode(int mode){

        if( mode < 1 || 3 < mode ){
            System.err.println("ERROR: no mode with id "+mode+".");
        }
        else {
            this.mode = mode;
        }
    }

    public void enterAblityMode(String abilityType){

        ability.selectAbility( abilityType );
        setMode(ABILITY_MODE);

    }

    public void switchMode(){

        if(mode == MOVMENT_MODE){
            mode = ATTACK_MODE;
        }
        else {
            mode = MOVMENT_MODE;
        }
        deselectTile();
    }

    private void initBattleGrid(int[][] map){

        this.tileGrid = new BattleGridTile[this.gridWidth][this.gridHeight];
        this.dijkstraGrid = new DijkstraGrid(map);


        for(int i =0; i < this.gridWidth; i++){
            for(int j = 0; j < this.gridHeight; j++){

                Vector tilePosition = initTilePosition(i,j);

                BattleGridTile newTile = new BattleGridTile(tilePosition, i, j);

                this.tileGrid[i][j] = newTile;
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

    public void addOccupentTo(int x, int y, BattleEntity occupent){

        addOccupentTo(getTile(x, y), occupent);

    }

    private void addOccupentTo(BattleGridTile tile, BattleEntity occupent){
        if( occupent instanceof Slime ) {
            tile.addOccupent(occupent);
        }
        else if ( occupent instanceof SlimeFactory ){
            tile.addOccupent(occupent);
            ((SlimeFactory)occupent).setSpawnableTiles(getAjacent(tile));
        }
//        gridState[tile.getxIndex()][tile.getyIndex()] = 0;
    }

    private int getTileX(BattleGridTile tile){
        return (int)( tile.getX() - xBuffer ) / tileSize;
    }
    private int getTileX(Vector position){
        return (int)( position.getX() - xBuffer ) / tileSize;
    }
    private int getTileY(BattleGridTile tile){
        return (int)( tile.getY() - yBuffer ) / tileSize;
    }
    private int getTileY(Vector position){
        return (int)( position.getY() - yBuffer ) / tileSize;
    }

    private IntVector getTileIndex(BattleGridTile tile){

        return new IntVector(getTileX(tile), getTileY(tile));

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

        return getTile(i, j);
    }

    public BattleGridTile getTile(int x, int y){

        if( x >= gridWidth || y >= gridHeight || x < 0 || y < 0 ){
            return null;
        }
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

        BattleGridTile tileInGrid = getTile( newTile.getxIndex(), newTile.getyIndex() );
        tileInGrid.replaceOccupent(newTile.getOccupent());
    }

    public void setTile( int x, int y, BattleGridTile tile ){
        tileGrid[x][y] = tile;
    }

    public boolean tileSelected(){

        return this.currentlySelectedTile != null;
    }

    public int getWinner() {
        return winner;
    }

    private void setWinner(int winner) {
        this.winner = winner;
    }

    public void checkSetWinner(){

        LinkedList<BattleEntity> entities = getEntityList();
        int playerOne = -1;

        for( int i = 0; i < entities.size(); i++ ){
            if(entities.get(i) instanceof  SlimeFactory){
                if( playerOne != -1 && playerOne != entities.get(i).getClientID() ){
                    return;
                }
                else if( playerOne != entities.get(i).getClientID() ){
                    playerOne = entities.get(i).getClientID();
                }
            }
        }
        setWinner(playerOne);
        background.cheer();
    }

    public void selectTile(Vector position){

        BattleGridTile tile = getTile(position);
        boolean attackOccured = false;

        if(tile != null) {

            int x = getTileX( tile );
            int y = getTileY( tile );

            if( !tileSelected() ){
                if(mode == ABILITY_MODE){
                    abilitySelect(x, y);
                    attackOccured = true;
//                    mode = MOVMENT_MODE;
                }
                else {
                    selectTile(x, y);
                }
            }
            else {
                if (currentlySelectedTile == tile) {
                    deselectTile();
                } else if (mode == MOVMENT_MODE) {
                    moveSelect(x,y);
                } else if (mode == ATTACK_MODE) {
                    attackSelect(x,y);
                    attackOccured = true;
                }
            }
        }
        if( attackOccured ){
            checkSetWinner();
        }
        System.out.println("I'm okay, mode: "+mode );

    }

    public void selectTile(int x, int y){

        if( getTile(x,y).hasOccupent() && getTile(x,y).getOccupent() instanceof Slime ) {
            distanceGrid = dijkstraGrid.getDistanceGrid(x, y);
            currentlySelectedTile = getTile(x, y);
            shadeInRange();
        }
    }

    public void deselectTile(){
        currentlySelectedTile = null;
        shadeInRange();
    }

    public void moveSelect( int x, int y ){

        if( inRange( this.currentlySelectedTile, x, y )){
            moveOccupent(this.currentlySelectedTile, getTile(x,y));
            deselectTile();
        }
    }

    public void attackSelect( int x, int y ){

        if( inRange( this.currentlySelectedTile, x, y )){

            if(((Slime)this.currentlySelectedTile.getOccupent()).hasAttacked()){
                return;
            }
            ArrayList<IntVector> pattern = ((Slime)this.currentlySelectedTile.getOccupent()).getAttackPattern(x,y);
            BattleGridTile effectedTile;
            Slime attackingSlime = ((Slime) currentlySelectedTile.getOccupent());
            BattleEntity defender;

            for( int i = 0; i < pattern.size(); i++ ){
                effectedTile = getTile( pattern.get(i).x, pattern.get(i).y );

                if(effectedTile != null) {
                    effectedTile.damageOccupent(attackingSlime.damage);
                    effectedTile.addSplash(attackingSlime.getClientID());
                    gameApi.createEntity(effectedTile);
                }
            }
            attackingSlime.onAttack();
            deselectTile();
        }
    }

    public void abilitySelect( int x, int y ){

        if(ability.getTargetingType() == BattleAbility.MASS_EFFECT) {
            ArrayList<BattleGridTile> effectedTiles = getOccupiedTiles();
        }
        else if(ability.getTargetingType() == BattleAbility.TARGETED_EFFECT){
            ArrayList<IntVector> pattern = ability.getAttackPattern(x, y);
            ArrayList<BattleGridTile> effectedTiles = new ArrayList<>();

            for( int i = 0; i < pattern.size(); i++){
                BattleGridTile effectedTile = getTile( pattern.get(i).x, pattern.get(i).y );

                if(effectedTile != null) {
                    effectedTiles.add(effectedTile);
//                    effectedTile.addSplash(ability.getCurrentPlayerId());
                }
            }
            ability.activateAbility(effectedTiles);

            for( int i = 0; i < effectedTiles.size(); i++){
                gameApi.createEntity(effectedTiles.get(i));
            }
        }
    }

    public BattleGridTile getSelectedTile(){
        return currentlySelectedTile;
    }

    private Slime getSelectedOccupent(){
        if( tileSelected() ){
            return (Slime)getSelectedTile().getOccupent();
        }
        return null;
    }

    private boolean inRange(int x, int y){
        return inRange(currentlySelectedTile, x, y);
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
        if( mode == ATTACK_MODE ) {
            if (((Slime) tile.getOccupent()).inRange(distanceGrid[x][y]) ) {
                return true;
            }
        }
        if( mode == MOVMENT_MODE ) {
            if (((Slime) tile.getOccupent()).getSpeed() >= distanceGrid[x][y]) {
                return true;
            }
        }
        return false;
    }

    private void shadeInRange(){

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                getTile(i, j).setShaded(inRange(currentlySelectedTile, i, j));
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
        if( ((Slime) a.getOccupent()).onCooldown() || ((Slime) a.getOccupent()).hasMoved()){
            return;
        }
        if(b.hasOccupent()){
            if(b.getOccupent() instanceof Slime && !a.getOccupent().equals(b.getOccupent()) &&
                    ((Slime) b.getOccupent()).clientID == ((Slime) a.getOccupent()).clientID &&
                    !((Slime) b.getOccupent()).isUpgraded() && !((Slime) a.getOccupent()).isUpgraded()){

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
        movingSlime.onMove();
        gameApi.createEntity(newTileB);
    }

    public LinkedList<BattleEntity> getEntityList(){
        LinkedList<BattleEntity> entityList = new LinkedList<>();

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                BattleGridTile tile = tileGrid[i][j];
                if(tile.hasOccupent()){
                    entityList.add( tile.getOccupent() );
                }
            }
        }
        return entityList;
    }

    public ArrayList<BattleGridTile> getOccupiedTiles(){

        ArrayList<BattleGridTile> tileList = new ArrayList<>();

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                BattleGridTile tile = tileGrid[i][j];
                if(tile.hasOccupent()){
                    tileList.add( tile );
                }
            }
        }
        return tileList;

    }

    public void highlightTile(Vector position, Graphics g){
        BattleGridTile tile = getTile(position);
        Color c = g.getColor();

        g.setColor(highlight);
        g.fillRect(tile.getX()-tileSize/2, tile.getY()-tileSize/2,
                tileSize, tileSize);

        g.setColor(c);
    }

    public void highlightAttackPattern( Vector position, Graphics g ){

        int x = getTileX(position);
        int y = getTileY(position);

        if ( !inRange(x,y) ){
            highlightTile(position, g);
            return;
        }
        ArrayList<IntVector> pattern = getSelectedOccupent().getAttackPattern(x,y);
        highlightPattern(pattern, g);
    }

    public void highlightAbilityPattern( Vector position, Graphics g ){

        int x = getTileX(position);
        int y = getTileY(position);

        ArrayList<IntVector> pattern = ability.getAttackPattern(x,y);
        highlightPattern(pattern, g);
    }

    public void highlightPattern(ArrayList<IntVector> pattern, Graphics g){
        for( int i = 0; i < pattern.size(); i++ ){
            int x = pattern.get(i).x;
            int y = pattern.get(i).y;

            BattleGridTile tile = getTile(x,y);
            if(tile != null){
                highlightTile(tile.getPosition(), g);
            }
        }
    }

    public void mouseoverHighlight( Vector position, Graphics g ){

        if( this.tileSelected() && mode == ATTACK_MODE){
            highlightAttackPattern(position, g);
        }
        else if(this.mode == ABILITY_MODE){
            if (ability.getTargetingType() == BattleAbility.TARGETED_EFFECT && !ability.used()){
                highlightAbilityPattern(position, g);
//                background.cheer();
            }
        }
        else{
            highlightTile(position, g);
        }

    }

    public void onNextTurn(){

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

        background.render(g);
        this.drawGrid(g);

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
                tile.render(g);

            }
        }
    }

    public void update(int delta){
        for( int i = 0; i < gridWidth; i++){
            for( int j = 0; j < gridHeight; j++){
                tileGrid[i][j].update(delta);
            }
        }
    }
}
