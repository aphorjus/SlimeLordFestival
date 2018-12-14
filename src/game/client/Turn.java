package game.client;

import game.api.GameApi;
import org.newdawn.slick.Game;

import java.util.LinkedList;

public class Turn {
    public static int NUM_MOVES = 10;
    public static int NUM_PLAYERS = 4;
    private int currentID;
    public int turnID;
    private int move;
    private GameApi gameApi;
    public LinkedList<Integer> loserIds = new LinkedList<>();

    public Turn(GameApi gameApi, int currentID) {
        this.gameApi = gameApi;
        this.currentID = currentID;
        this.turnID = 0;
        move = 0;
    }

    public boolean makeMove(){
        if(move < NUM_MOVES) {
            move++;
            if(move == NUM_MOVES){
                gameApi.endTurn();
                return false;
            }
            return true;
        }
        return false;
    }

    public void updateMoves(int delta){
        move = move + delta;
        if(move == NUM_MOVES){
            gameApi.endTurn();
        }
    }

    public int getMove(){
        return move;
    }

    public boolean isMyMove() {
        //System.out.println(currentID + " " + turnID + " " + move);
        return currentID == turnID && move < NUM_MOVES;
    }

    public void addLoser(int id) {
        loserIds.add(id);
    }

    // called by gameApi to let players know that the current turn has ended.
    public void turnHasEnded(GameClient gc) {
        turnID = (turnID + 1) % gc.players.length;
        move = 0;

        boolean isLoser = false;
        for (int id : loserIds) {
            if (id == turnID) isLoser = true;
        }

        if (isLoser) {
            turnHasEnded(gc);
        }
    }

    public int getCurrentPlayer(){
        return currentID;
    }
}
