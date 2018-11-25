package game;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Comparator;

public class DijkstraGrid {

    public final int OBSTRUCTION = 0;

    int gridWidth;
    int gridHeight;

    DijkstraTile[][] tileGrid;
    double[][] distanceGrid;

    public DijkstraGrid(int[][] weights){
        gridWidth = weights.length;
        gridHeight = weights[0].length;

        initTileGrid(weights);
    }

    private void initTileGrid(int[][] weights){
        tileGrid = new DijkstraTile[gridWidth][gridHeight];

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                tileGrid[i][j] = new DijkstraTile( i, j, weights[i][j] );
            }
        }
    }

    private boolean isCorner( int orentation ){
        int[] corners = {0, 2, 5, 7};

        for(int i = 0; i < corners.length; i++){
            if( orentation == corners[i] ){
                return true;
            }
        }
        return false;
    }

    private double getEdgeWeight(DijkstraTile a, DijkstraTile b, int orentaion){

        if( isCorner(orentaion) ){
            return Math.sqrt((a.getWeight()*b.getWeight()) + (b.getWeight()*b.getWeight()));
        }
        return (a.getWeight()/2) + (b.getWeight()/2);
    }

    private DijkstraTile[] getAjacent(DijkstraTile tile){

        int x = tile.getXPosition();
        int y = tile.getYPosition();
        int i = 0;

        DijkstraTile[] ajacent = new DijkstraTile[8];

        for (int xOffset = -1; xOffset <= 1; xOffset++){
            for (int yOffset = -1; yOffset <= 1; yOffset++){

                if( yOffset == 0 && xOffset == 0 ){
                    continue;
                }
                if( x+xOffset >= 0         && y+yOffset >= 0 &&
                    x+xOffset <  gridWidth && y+yOffset <  gridHeight ){

                    DijkstraTile thisTile = tileGrid[x+xOffset][y+yOffset];

                    if( thisTile.getWeight() == OBSTRUCTION ){
                        ajacent[i] = null;
                    }
                    else{
                        ajacent[i] = thisTile;
                    }
                    i++;
                }
            }
        }
//        System.out.println(Arrays.toString(ajacent));
        return  ajacent;
    }

    private static Comparator<DijkstraTile> dtComparator = new Comparator<DijkstraTile>() {
        @Override
        public int compare(DijkstraTile a, DijkstraTile b){
            return (int)(a.getDistance() - b.getDistance());
        }
    };

    private PriorityQueue<DijkstraTile> initQ(int x, int y) {
        PriorityQueue<DijkstraTile> Queue = new PriorityQueue<DijkstraTile>( dtComparator );

        for(int i = 0; i < gridWidth; i++){
            for( int j = 0; j < gridHeight; j++){
                DijkstraTile tile = tileGrid[i][j];

                if( tile.weight == OBSTRUCTION ){
                    continue;
                }
                if(i == x && j == y){
                    tile.setDistance(0);
                    tile.setNext(tile);
                    Queue.add(tile);
                }
                else{
                    tile.setDistance(Double.MAX_VALUE);
                    Queue.add(tile);
                }
            }
        }
        return Queue;
    }

    private void dijkstra(int x, int y){

        PriorityQueue<DijkstraTile> Queue = initQ(x,y);
        double tempDist;
        double edgeWeight;

        while(!Queue.isEmpty()){
            DijkstraTile u = Queue.poll();
//            System.out.println(u.getDistance());

            DijkstraTile[] ajacent = getAjacent(u);
            for(int i = 0; i < ajacent.length; i++){
                if(ajacent[i] != null){
                    edgeWeight = getEdgeWeight(ajacent[i], u, i);
                    tempDist = u.getDistance() + edgeWeight;

                    if (tempDist < ajacent[i].getDistance()){
                        ajacent[i].setDistance(tempDist);
                        ajacent[i].setNext(u);
                        u.setPrevious(ajacent[i]);
//                        System.out.println(ajacent[i].getDistance()+" added");
                        Queue.add(ajacent[i]);
                    }
                }
            }
        }
    }

    public double[][] getDistanceGrid(int x, int y){


        dijkstra(x,y);

        distanceGrid = new double[gridWidth][gridHeight];

        for(int i = 0; i < gridWidth; i++){
            for(int j = 0; j < gridHeight; j++){
                distanceGrid[i][j] = tileGrid[i][j].getDistance();
            }
        }
//        printDistanceGrid();
        return distanceGrid;

    }

    public void printDistanceGrid(){
        for(int i = 0; i < distanceGrid.length; i++) {
            for (int j = 0; j < distanceGrid[0].length; j++) {
                double dist = distanceGrid[i][j];
                if(dist == Double.MAX_VALUE){
                    System.out.printf("< MAAX >");
                }
                else
                    System.out.printf("< %.2f >", distanceGrid[i][j]);
            }
            System.out.println();
        }
    }

}
