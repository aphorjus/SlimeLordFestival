package game.entities.building;

import game.client.Button;
import game.client.GameClient;
import game.entities.slimelord.SlimeLord;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;


import java.awt.*;
import java.util.Arrays;

public class Shop{
    Graphics g = null;
    Image currentImage = null;
    Image buyImage = null;
    Image soldImage = null;
    Image shopkeeper = null;
    Button buyStriker = null;
    Button buyLancer = null;
    Button buyAdvancedStriker = null;
    SlimeLord currentSlimeLord = null;
    String currentMessage = "Welcome to my shop Slime Lord!";
    public static final String LOBBYBOARD = "game/client/resource/LobbyBoard.png";
    public static final String BUYIMAGE = "game/client/resource/BuyButton.png";
    public static final String SOLDIMAGE = "game/client/resource/SOLD.png";
    public static final String SHOPKEEPER = "game/client/resource/ShopKeeper1.png";
    GameClient currentG = null;


    public Shop(GameClient bg){
        currentG = bg;
        try{
            currentImage = new Image(LOBBYBOARD);
            buyImage = new Image(BUYIMAGE);
            soldImage = new Image(SOLDIMAGE);
            shopkeeper = new Image(SHOPKEEPER);
        } catch ( Exception e){
            e.printStackTrace();
        }
        buyStriker = new Button(282, 190, buyImage);
        buyLancer = new Button(454, 190, buyImage);
        buyAdvancedStriker = new Button(626, 190, buyImage);
    }

    public void setCurrentSlimeLord(SlimeLord nsl){
        currentSlimeLord = nsl;
    }

    public void checkClick(int x, int y) {

        if(buyStriker.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("striker") == false){
                if(currentG.getTokens() >= 100){
                    currentG.setTokens(currentG.getTokens() - 100);
                    currentMessage = "Nice purchase!";
                    buyStriker.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("striker");
                }else{
                    currentMessage = "Sorry, but it looks like you can't afford that!";
                }
            } else{ //If you already own it
                currentMessage = "You already own that my dude!";
            }
        }else if(buyLancer.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("lancer") == false){
                if(currentG.getTokens() >= 100){
                    currentG.setTokens(currentG.getTokens() - 100);
                    currentMessage = "Nice purchase!";
                    buyLancer.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("lancer");
                }else{
                    currentMessage = "Sorry, but it looks like you can't afford that!";
                }
            } else{ //If you already own it
                currentMessage = "You already own that my dude!";
            }
        }else if(buyAdvancedStriker.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("advancedLancer") == false){
                if(currentG.getTokens() >= 300){
                    currentG.setTokens(currentG.getTokens() - 300);
                    currentMessage = "Nice purchase!";
                    buyAdvancedStriker.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("advancedLancer");
                }else{
                    currentMessage = "Sorry, but it looks like you can't afford that!";
                }
            } else{ //If you already own it
                currentMessage = "You already own that my dude!";
            }
        }

    }
    public void render(Graphics g) {
        g.drawImage(currentImage,0,0);
        g.drawImage(shopkeeper,221,275);
        g.drawString(currentMessage,350,350);
        buyStriker.render(g);
        buyLancer.render(g);
        buyAdvancedStriker.render(g);
    }
}
