package game.client;

public class Turn {
    public static int NUM_MOVES = 10;
    public static int NUM_PLAYERS = 4;
    private int currentID;
    private int turnID;
    private int move;

    public Turn(int currentID) {
        this.currentID = currentID;
        this.turnID = 0;
        move = 0;
    }

    public boolean makeMove(){
        if(move < NUM_MOVES) {
            move++;
            if(move == NUM_MOVES){
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isMyMove() {
        return currentID == turnID && move < NUM_MOVES;
    }

    public void turnHasEnded() {
        turnID = (turnID) % NUM_PLAYERS;
        move = 0;
    }

    public int getCurrentPlayer(){
        return currentID;
    }
}
