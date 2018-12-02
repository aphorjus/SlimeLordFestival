package game.client;

public class Turn {
    public static int NUM_MOVES = 10;
    public static int NUM_PLAYERS = 4;
    private int currentID;
    private int move;

    public Turn() {
        currentID = 1;
        move = 0;
    }

    public boolean makeMove(){
        if(move < NUM_MOVES) {
            move++;
            if(move == NUM_MOVES){
                currentID = 1 + (currentID) % NUM_PLAYERS;
            }
            return true;
        }
        return false;
    }

    public int getCurrentPlayer(){
        return currentID;
    }
}
