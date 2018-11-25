package game.Battles;

import game.client.Board;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

public class BattleGridTile extends Entity {

    public Entity occupent;
    public Vector position;

    public BattleGridTile(Vector position){
        super(position);
        this.position = position;
        this.occupent = null;
        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    public boolean addOccupent(Entity newOccupent){
        if( !this.hasOccupent() ) {
            this.occupent = newOccupent;
            this.occupent.setPosition(this.position);
            return true;
        }
        return false;
    }

    public void removeOccupent(){
        if( this.hasOccupent() ){
            this.occupent = null;
        }
    }

    public void replaceOccupent( Entity newOccupent ){
        this.removeOccupent();
        this.addOccupent( newOccupent );
    }

    public boolean hasOccupent(){
        return this.occupent != null;
    }

    public void renderOccupent(Graphics g){
        if (this.hasOccupent()){
            this.occupent.render(g);
        }
    }
}
