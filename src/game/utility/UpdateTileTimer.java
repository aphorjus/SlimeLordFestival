package game.utility;

import game.Battles.BattleGridTile;
import game.api.GameApi;
import jig.Vector;

public class UpdateTileTimer extends Thread {
    BattleGridTile newTile;
    int time;
    GameApi gameApi;

    public UpdateTileTimer(GameApi gameApi, BattleGridTile newTile, int time) {
        this.newTile = newTile;
        this.time = time;
        this.gameApi = gameApi;
    }

    @Override
    public void run() {
        try {
            sleep(time);
            gameApi.createEntity(newTile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
