package game.Battles;

import game.client.Board;
import game.entities.IEntity;
import game.entities.slime.Slime;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;

public class BattleGridTile extends Entity implements IEntity {

    public IEntity occupent;
    public Vector position;
//    public int xIndex;
//    public int yIndex;
    public boolean shaded;

    public BattleGridTile(Vector position){//, int x, int y){
        super(position);
        this.position = position;
        this.occupent = null;
        this.shaded = false;
        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    public BattleGridTile(JSONObject jasonTile){

        super(new Vector( jasonTile.getFloat("xPosition"),
                jasonTile.getFloat("yPosition") ));

        if( jasonTile.has("occupent") ){
            // CHANGE DONSE NOT SUPPORT OTHER OCCUPENT TYPES
            this.occupent = new Slime(jasonTile.getJSONObject("occupent"));
        }
        this.shaded = jasonTile.getBoolean("shaded");
    }

    @Override
    public String getEntityType() {
        return "BattleGridTile";
    }

    @Override
    public JSONObject toJson() {
        JSONObject jasonTile = new JSONObject();

        if( this.hasOccupent() ) {
            jasonTile.put("occupent", occupent.toJson());
        }
        jasonTile.put("xPosition", position.getX());
        jasonTile.put("yPosition", position.getY());
        jasonTile.put("shaded", shaded);
        this.addImage(ResourceManager.getImage(Board.TILE_RSC));

        return jasonTile;
    }

    public IEntity getOccupent(){
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

    public boolean addOccupent(IEntity newOccupent){
        if( !this.hasOccupent() ) {
            this.occupent = newOccupent;
            ((Entity)this.occupent).setPosition(this.position);
            return true;
        }
        return false;
    }

    public void removeOccupent(){
        if( this.hasOccupent() ){
            this.occupent = null;
        }
    }

    public void replaceOccupent( IEntity newOccupent ){
        this.removeOccupent();
        this.addOccupent( newOccupent );
    }

    public boolean hasOccupent(){

        return this.occupent != null;
    }

    public void renderOccupent(Graphics g){
        if (this.hasOccupent()){
            ((Entity)this.occupent).render(g);
        }
    }
}
