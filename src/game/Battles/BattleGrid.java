package game.Battles;


import game.entities.slime.Slime;
import jig.Vector;
import org.newdawn.slick.Graphics;

public class BattleGrid {

    public int gridHeight;
    public int gridWidth;
    public int screenHeight;
    public int screenWidth;
    public int gridPixleHeight;
    public int gridPixleWidth;

    public int tileHeight;
    public int tileWidth;
    public int tileSize;

    public int xBuffer;
    public int yBuffer;

    private BattleGridTile[][] tileGrid;

    public BattleGrid(final int screenHeight, final int screenWidth,
                      int yBuffer, int xBuffer, final int[][] map){

        this.screenHeight = screenHeight;
        this.yBuffer = yBuffer;
        this.gridPixleHeight = screenHeight - (2*yBuffer);
        this.gridHeight = map[0].length;

        this.tileSize = gridPixleHeight / gridHeight;

        this.screenWidth = screenWidth;
        this.gridWidth = map.length;
        this.xBuffer = ( screenWidth - ( gridWidth*tileSize )) / 2;
        this.gridPixleWidth = screenWidth - (2*xBuffer);
        this.tileWidth = gridPixleWidth / gridWidth;

        initBattleGrid(map);
    }

    private void initBattleGrid(int[][] map){

        this.tileGrid = new BattleGridTile[this.gridWidth][this.gridHeight];

        for(int i =0; i < this.gridWidth; i++){
            for(int j = 0; j < this.gridHeight; j++){

                Vector tilePosition = initTilePosition(i,j);

                BattleGridTile newTile = new BattleGridTile(tilePosition);

                this.tileGrid[i][j] = newTile;

                if( map[i][j] == 1 ){
                    newTile.addOccupent(new Slime(1, tilePosition));
                }
            }
        }
    }

    private Vector initTilePosition(int i, int j){

        int x = (tileSize*i)+(tileSize/2)+xBuffer;
        int y = (tileSize*j)+(tileSize/2)+yBuffer;

        return new Vector(x,y);
    }

    public BattleGridTile getTile(Vector position){

        int i = (int)(position.getX()-xBuffer) / tileSize;
        int j = (int)(position.getY()-yBuffer) / tileSize;

        return this.tileGrid[i][j];
    }

    public void render(Graphics g){
        for(int i = 0; i < this.gridWidth; i++){
            for( int j = 0; j < this.gridHeight; j++){
                BattleGridTile tile = this.tileGrid[i][j];

                tile.render(g);
                tile.renderOccupent(g);
            }
        }
    }
}
