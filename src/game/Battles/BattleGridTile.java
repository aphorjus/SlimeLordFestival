package game.Battles;

import game.client.Board;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.newdawn.slick.Graphics;

public class BattleGridTile extends Entity {

    public Entity occupent;
    public Vector position;
    public boolean shaded;

    public BattleGridTile(Vector position){
        super(position);
        this.position = position;
        this.occupent = null;
        this.shaded = false;
        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    public Entity getOccupent(){
        return this.occupent;
    }

    public boolean isShaded(){
        return shaded;
    }

    public void setShaded( boolean shaded ){
        if(this.shaded == shaded){
            return;
        }
        this.shaded = shaded;
        if( shaded ){
            this.addImage(ResourceManager.getImage(Board.SLIME2_RSC));
        }
        else{
            this.removeImage(ResourceManager.getImage(Board.SLIME2_RSC));
        }
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
