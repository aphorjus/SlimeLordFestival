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
    Integer currentShopkeeper = null;
    Image shopkeeper = null;
    Image shopkeeper2 = null;
    Image strikerImage = null;
    Image lancerImage = null;
    Image advancedStrikerImage = null;
    Image advancedLancerImage = null;
    Image mortarImage = null;
    Button buyStriker = null;
    Button buyLancer = null;
    Button buyAdvancedStriker = null;
    Button buyAdvancedLancer = null;
    Button buyMortar = null;
    SlimeLord currentSlimeLord = null;
    String currentMessage = "Welcome to my shop Slime Lord!";
    public static final String LOBBYBOARD = "game/client/resource/LobbyBoard.png";
    public static final String BUYIMAGE = "game/client/resource/BuyButton.png";
    public static final String SOLDIMAGE = "game/client/resource/SOLD.png";
    public static final String SHOPKEEPER = "game/client/resource/ShopKeeper1.png";
    public static final String SHOPKEEPER2 = "game/client/resource/ShopKeeper2.png";

    public static final String STRIKER = "game/client/resource/ShopStriker.png";
    public static final String LANCER = "game/client/resource/ShopLancer.png";
    public static final String ADVANCEDLANCER = "game/client/resource/ShopLancer.png";
    public static final String ADVANCEDSTRIKER = "game/client/resource/ShopStriker.png";
    public static final String MORTAR = "game/client/resource/ShopStriker.png";

    GameClient currentG = null;


    public Shop(GameClient bg){
        currentG = bg;
        try{
            currentImage = new Image(LOBBYBOARD);
            buyImage = new Image(BUYIMAGE);
            soldImage = new Image(SOLDIMAGE);
            shopkeeper = new Image(SHOPKEEPER);
            shopkeeper2 = new Image(SHOPKEEPER2);
            lancerImage = new Image(LANCER);
            strikerImage = new Image(STRIKER);
            advancedLancerImage = new Image(ADVANCEDLANCER);
            advancedStrikerImage = new Image(ADVANCEDSTRIKER);
            mortarImage = new Image(MORTAR);
            currentShopkeeper = 1;

        } catch ( Exception e){
            e.printStackTrace();
        }
        buyStriker = new Button(260, 190, buyImage);
        buyLancer = new Button(360, 190, buyImage);
        buyAdvancedStriker = new Button(460, 190, buyImage);
        buyAdvancedLancer = new Button(560, 190, buyImage);
        buyMortar = new Button(660, 190, buyImage);
    }

    public void setCurrentSlimeLord(SlimeLord nsl){
        currentSlimeLord = nsl;
    }

    public void exitShop(){
        currentMessage = "Welcome back slime lord, did ya miss me?";
        currentShopkeeper = 1;
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
                    currentShopkeeper = 2;
                }else{
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else{ //If you already own it
                currentMessage = "You already own that my dude!";
                currentShopkeeper = 1;
            }
        }else if(buyLancer.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("lancer") == false){
                if(currentG.getTokens() >= 100){
                    currentG.setTokens(currentG.getTokens() - 100);
                    currentMessage = "Nice purchase!";
                    buyLancer.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("lancer");
                    currentShopkeeper = 2;
                }else{
                    currentShopkeeper = 1;
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                }
            } else{ //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buyAdvancedStriker.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("advancedStriker") == false){
                if(currentG.getTokens() >= 300){
                    currentG.setTokens(currentG.getTokens() - 300);
                    currentMessage = "Nice purchase!";
                    buyAdvancedStriker.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("advancedStriker");
                    currentShopkeeper = 2;
                }else{
                    currentShopkeeper = 1;
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                }
            } else{ //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buyAdvancedLancer.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("advancedLancer") == false){
                if(currentG.getTokens() >= 300){
                    currentG.setTokens(currentG.getTokens() - 300);
                    currentMessage = "Nice purchase!";
                    buyAdvancedLancer.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("advancedLancer");
                    currentShopkeeper = 2;
                }else{
                    currentShopkeeper = 1;
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                }
            } else{ //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buyMortar.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.specialSlimes.contains("mortar") == false){
                if(currentG.getTokens() >= 600){
                    currentG.setTokens(currentG.getTokens() - 600);
                    currentMessage = "Nice purchase!";
                    buyMortar.setCurrentImage(soldImage);
                    currentSlimeLord.specialSlimes.add("mortar");
                    currentShopkeeper = 2;
                }else{
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else{ //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }

    }
    public void render(Graphics g) {
        g.drawImage(currentImage,0,0);
        if(currentShopkeeper == 1){
            g.drawImage(shopkeeper,221,245);
        }else{
            g.drawImage(shopkeeper2,225,270);
        }

        g.drawString(currentMessage,355,350);
        buyStriker.render(g);
        buyLancer.render(g);
        buyAdvancedStriker.render(g);
        buyAdvancedLancer.render(g);
        buyMortar.render(g);

        g.drawImage(strikerImage, 270,150);
        g.drawImage(lancerImage, 370,150);
        g.drawImage(advancedStrikerImage, 470,150);
        g.drawImage(advancedLancerImage, 570,150);
        g.drawImage(mortarImage, 670,150);

        /*
        buyStriker = new Button(260, 190, buyImage);
        buyLancer = new Button(360, 190, buyImage);
        buyAdvancedStriker = new Button(460, 190, buyImage);
        buyAdvancedLancer = new Button(560, 190, buyImage);
        buyMortar = new Button(660, 190, buyImage);
        */
    }
}
