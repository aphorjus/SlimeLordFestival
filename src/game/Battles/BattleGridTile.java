package game.Battles;

import game.client.Board;
import game.entities.IEntity;
import game.entities.slime.Slime;
import game.entities.slimefactory.SlimeFactory;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import org.json.JSONObject;
import org.newdawn.slick.Graphics;

public class BattleGridTile extends Entity implements IEntity {

    private String entityType = "BattleGridTile";
    private BattleEntity occupent;
//    public Vector position;
    private int xIndex;
    private int yIndex;
    private boolean shaded;

    public BattleGridTile(Vector position, int x, int y){
        super(position);
//        this.position = position;
        this.xIndex = x;
        this.yIndex = y;
        this.occupent = null;
        this.shaded = false;
//        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    public BattleGridTile(JSONObject jsonTile){

//        super(new Vector( jsonTile.getFloat("xPosition"), jsonTile.getFloat("yPosition") ));
//
//        this.position = getPosition();
        this.xIndex = jsonTile.getInt("xIndex");
        this.yIndex = jsonTile.getInt("yIndex");
        this.shaded = jsonTile.getBoolean("shaded");

        if( jsonTile.has("occupent") ){
            if( jsonTile.getJSONObject("occupent").getString("entityType").equals("Slime") ) {
                this.addOccupent(new Slime(jsonTile.getJSONObject("occupent")));
            }
            else if( jsonTile.getJSONObject("occupent").getString("entityType").equals("Factory") ){
                this.addOccupent(new SlimeFactory(jsonTile.getJSONObject("occupent")));
            }
            else {
                System.err.println(((IEntity)occupent).getEntityType()+" is not a valid type for 'occupent'");
            }
        }
//        this.addImage(ResourceManager.getImage(Board.TILE_RSC));
    }

    @Override
    public String getEntityType() {

        return entityType;
    }

    @Override
    public JSONObject toJson() {
        JSONObject jsonTile = new JSONObject();

        jsonTile.put("entityType", getEntityType());
        if( this.hasOccupent() ) {
            jsonTile.put("occupent", ((IEntity)occupent).toJson());
        }
        jsonTile.put("xPosition", this.getPosition().getX());
        jsonTile.put("yPosition", this.getPosition().getY());

        jsonTile.put("xIndex", xIndex);
        jsonTile.put("yIndex", yIndex);
        jsonTile.put("shaded", shaded);

        return jsonTile;
    }

    public int getxIndex() {
        return xIndex;
    }

    public int getyIndex() {
        return yIndex;
    }

    public BattleEntity getOccupent(){
        return this.occupent;
    }

    public boolean isShaded(){
        return shaded;
    }

    public void setShaded( boolean shaded ){

        this.shaded = shaded;
    }

    public boolean addOccupent(BattleEntity newOccupent){
        if( !this.hasOccupent() && newOccupent != null ) {
            this.occupent = newOccupent;
            ((Entity)this.occupent).setPosition(this.getPosition());
            return true;
        }
        return false;
    }

    public void removeOccupent(){
        if( this.hasOccupent() ){
            this.occupent = null;
        }
    }

    public void setOccupent(BattleEntity occupent){
        this.occupent = occupent;
        if(hasOccupent()){
            ((Entity)this.occupent).setPosition(this.getPosition());
        }
    }

    public void replaceOccupent( BattleEntity newOccupent ){
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
