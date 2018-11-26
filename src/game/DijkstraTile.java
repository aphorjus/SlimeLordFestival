package game;

public class DijkstraTile {

    public double weight;
    public double distance;
    public DijkstraTile next;
    public DijkstraTile previous;
    private int xPosition;
    private int yPosition;

    public DijkstraTile(int x, int y, int weight){
        this.xPosition = x;
        this.yPosition = y;
        this.weight = weight;
        this.distance = -1;
    }
    public int getXPosition(){
        return this.xPosition;
    }
    public int getYPosition(){
        return this.yPosition;
    }
    public double getWeight(){
        return this.weight;
    }
    public double getDistance(){
        return this.distance;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public DijkstraTile getNext(){
        return this.next;
    }
    public void setNext(DijkstraTile next){
        this.next = next;
    }
    public DijkstraTile getPrevious(){
        return this.previous;
    }
    public void setPrevious(DijkstraTile previous){
        this.previous = previous;
    }
}
