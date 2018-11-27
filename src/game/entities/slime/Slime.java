package game.entities.slime;

import game.Battles.BattleGridTile;
import game.client.Board;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Slime extends Entity {

    public int clientID;
    public int hp;
    public int speed;
    public int size;

    public Slime(int size, Vector position, int id){
        super(position);
        this.clientID = id;
        this.size = size;
        this.speed = (int)((10/size) + 1);
        this.hp = 10*size;
        this.addImage(ResourceManager.getImage(Board.SLIME1_RSC));
    }

    public Slime combine(Slime slime){
        return new Slime(this.size + slime.size, slime.getPosition(), this.clientID );
    }
}

