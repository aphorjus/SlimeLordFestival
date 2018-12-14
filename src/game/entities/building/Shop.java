package game.entities.building;

import game.api.GameApi;
import game.client.Button;
import game.client.GameClient;
import game.entities.ShopkeepAnimation;
import game.entities.slimelord.SlimeLord;
import jig.Vector;
import org.newdawn.slick.Image;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;


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

    Image slimeStrikeImage = null;
    Image slimeBallImage = null;
    Image massHealImage = null;
    Image summonBasicImage = null;
    Image summonLancerImage = null;

    Button buySlimeStrike = null;
    Button buySlimeBall = null;
    Button buyMassHeal = null;
    Button buySummonBasicSlime = null;
    Button buySummonLancer = null;
    ShopkeepAnimation idleA = new ShopkeepAnimation(new Vector(290, 345));
    GameApi currentGA = null;
    SlimeLord currentSlimeLord = null;
    public Boolean viewPrices = null;
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

    public static final String SLIMESTRIKE = "game/client/resource/SlimeStrike.png";
    public static final String SLIMEBALL = "game/client/resource/SlimeBall.png";
    public static final String MASSHEAL = "game/client/resource/MassHeal.png";
    public static final String SUMMONBASIC = "game/client/resource/BasicSummon.png";
    public static final String SUMMONLANCER = "game/client/resource/LancerSummon.png";

    GameClient currentG = null;


    public Shop(GameClient bg, GameApi ga){
        currentG = bg;
        currentGA = ga;
        try{
            currentImage = new Image(LOBBYBOARD);
            buyImage = new Image(BUYIMAGE);
            soldImage = new Image(SOLDIMAGE);
            shopkeeper = new Image(SHOPKEEPER);
            shopkeeper2 = new Image(SHOPKEEPER2);
            lancerImage = new Image(LANCER);
            strikerImage = new Image(STRIKER);

            slimeBallImage = new Image(SLIMEBALL);
            slimeStrikeImage = new Image(SLIMESTRIKE);
            massHealImage = new Image(MASSHEAL);
            summonBasicImage = new Image(SUMMONBASIC);
            summonLancerImage = new Image(SUMMONLANCER);

            advancedLancerImage = new Image(ADVANCEDLANCER);
            advancedStrikerImage = new Image(ADVANCEDSTRIKER);
            mortarImage = new Image(MORTAR);
            currentShopkeeper = 1;

        } catch ( Exception e){
            e.printStackTrace();
        }
        buyStriker = new Button(260, 180, buyImage);
        buyLancer = new Button(360, 180, buyImage);
        buyAdvancedStriker = new Button(460, 180, buyImage);
        buyAdvancedLancer = new Button(560, 180, buyImage);
        buyMortar = new Button(660, 180, buyImage);

        buySlimeStrike = new Button(260, 280, buyImage);
        buySlimeBall = new Button(360, 280, buyImage);
        buyMassHeal = new Button(460, 280, buyImage);
        buySummonBasicSlime = new Button(560, 280, buyImage);
        buySummonLancer = new Button(660, 280, buyImage);

    }

    public void setCurrentSlimeLord(SlimeLord nsl){
        currentSlimeLord = nsl;
    }

    public void setAPI(GameApi nA){
        currentGA = nA;
    }

    public void exitShop(){
        currentMessage = "Welcome back slime lord, did ya miss me?";
        currentShopkeeper = 1;
        //currentGA.createEntity(currentSlimeLord);
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
        }else if(buySlimeStrike.checkClick(x,y) == true){
            //If you can buy it
            if(currentSlimeLord.abilities.contains("slimeStrike") == false){
                if(currentG.getTokens() >= 500){
                    currentG.setTokens(currentG.getTokens() - 500);
                    currentMessage = "Nice purchase!";
                    buySlimeStrike.setCurrentImage(soldImage);
                    currentSlimeLord.abilities.add("slimeStrike");
                    currentShopkeeper = 2;
                }else{
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else{ //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buySlimeBall.checkClick(x,y) == true) {
            //If you can buy it
            if (currentSlimeLord.abilities.contains("slimeBall") == false) {
                if (currentG.getTokens() >= 500) {
                    currentG.setTokens(currentG.getTokens() - 500);
                    currentMessage = "Nice purchase!";
                    buySlimeBall.setCurrentImage(soldImage);
                    currentSlimeLord.abilities.add("slimeBall");
                    currentShopkeeper = 2;
                } else {
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else { //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buyMassHeal.checkClick(x,y) == true) {
            //If you can buy it
            if (currentSlimeLord.abilities.contains("massHeal") == false) {
                if (currentG.getTokens() >= 500) {
                    currentG.setTokens(currentG.getTokens() - 500);
                    currentMessage = "Nice purchase!";
                    buyMassHeal.setCurrentImage(soldImage);
                    currentSlimeLord.abilities.add("massHeal");
                    currentShopkeeper = 2;
                } else {
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else { //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buySummonBasicSlime.checkClick(x,y) == true) {
            //If you can buy it
            if (currentSlimeLord.abilities.contains("summonBasicSlime") == false) {
                if (currentG.getTokens() >= 500) {
                    currentG.setTokens(currentG.getTokens() - 500);
                    currentMessage = "Nice purchase!";
                    buySummonBasicSlime.setCurrentImage(soldImage);
                    currentSlimeLord.abilities.add("summonBasicSlime");
                    currentShopkeeper = 2;
                } else {
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else { //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }else if(buySummonLancer.checkClick(x,y) == true) {
            //If you can buy it
            if (currentSlimeLord.abilities.contains("summonLancer") == false) {
                if (currentG.getTokens() >= 500) {
                    currentG.setTokens(currentG.getTokens() - 500);
                    currentMessage = "Nice purchase!";
                    buySummonLancer.setCurrentImage(soldImage);
                    currentSlimeLord.abilities.add("summonLancer");
                    currentShopkeeper = 2;
                } else {
                    currentMessage = "Sorry pal, looks like you can't afford that!";
                    currentShopkeeper = 1;
                }
            } else { //If you already own it
                currentShopkeeper = 1;
                currentMessage = "You already own that my dude!";
            }
        }
    }
    public void render(Graphics g) {
        g.drawImage(currentImage,0,0);
        g.drawString("Slimes",465,90);
        g.drawString("Abilities",455,215);

        if(currentShopkeeper == 1){
            //g.drawImage(shopkeeper,230,300);
            idleA.render(g);
        }else{
            g.drawImage(shopkeeper2,230,300);
        }

        g.setColor(Color.orange);
        g.drawString("Slime\nStrike",260,240);
        g.drawString("Slime\nBall",360,240);
        g.drawString("Mass\nHeal",460,240);
        g.drawString("Summon\nBasic",560,240);
        g.drawString("Summon\nLancer",660,240);

        //g.drawImage(slimeStrikeImage, 260, 220);
        //g.drawImage(slimeBallImage, 260, 220);
        //g.drawImage(massHealImage, 260, 220);
        //g.drawImage(summonBasicImage, 260, 220);
        //g.drawImage(summonLancerImage, 260, 220);
        g.drawString("Striker",260,130);
        g.drawString("Lancer",360,130);
        g.drawString("Advanced\nStriker",460,110);
        g.drawString("Advanced\nLancer",560,110);
        g.drawString("Mortar",660,130);
        g.setColor(Color.white);
        g.drawImage(lancerImage,270,140);
        g.drawImage(lancerImage,370,140);
        g.drawImage(lancerImage,470,140);
        g.drawImage(lancerImage,570,140);
        g.drawImage(lancerImage,670,140);


        g.drawString(currentMessage,355,350);
        g.drawString("(hold \"P\" to view prices)", 392,374);
        if(viewPrices == true){
            g.drawString("$100",265,180);
            g.drawString("$100",365,180);
            g.drawString("$300",465,180);
            g.drawString("$300",565,180);
            g.drawString("$600",665,180);

            g.drawString("$500",265,280);
            g.drawString("$500",365,280);
            g.drawString("$500",465,280);
            g.drawString("$500",565,280);
            g.drawString("$500",665,280);

        }else{
            buyStriker.render(g);
            buyLancer.render(g);
            buyAdvancedStriker.render(g);
            buyAdvancedLancer.render(g);
            buyMortar.render(g);

            buySlimeStrike.render(g);
            buySlimeBall.render(g);
            buyMassHeal.render(g);
            buySummonLancer.render(g);
            buySummonBasicSlime.render(g);
        }

        //g.drawString("Tokens:" + String.valueOf(currentG.getTokens()),900,480);

        /*

        buySlimeStrike = new Button(260, 250, buyImage);
        buySlimeBall = new Button(360, 250, buyImage);
        buyMassHeal = new Button(460, 250, buyImage);
        buySummonBasicSlime = new Button(560, 250, buyImage);
        buySummonLancer = new Button(660, 250, buyImage);

        buyLancer = new Button(360, 190, buyImage);
        buyAdvancedStriker = new Button(460, 190, buyImage);
        buyAdvancedLancer = new Button(560, 190, buyImage);
        buyMortar = new Button(660, 190, buyImage);

       g.drawString("Tokens:" + String.valueOf(bg.getTokens()),900,480);
       case "damage":           selectDamage();             break;
       case "slimeStrike":      selectSlimeStrike();        break;
       case "slimeBall":        selectSlimeBall();          break;
       case "massHeal":         selectMassHeal();
       case "summonBasicSlime": selectSummonBasicSlime();   break;
       case "summonLancer":     selectSummonLancer();       break;

        */
    }
}
