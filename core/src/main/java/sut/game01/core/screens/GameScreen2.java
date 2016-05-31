package sut.game01.core.screens;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import playn.core.CanvasImage;
import sut.game01.core.characters.*;
import sut.game01.core.item.arrow.Arrow;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.*;

public class GameScreen2 extends Screen{

    private GameScreen2 gameScreen2;
    //private GameScreen3 gameScreen3;

    //=======================================================
    // define for screen

    private ScreenStack ss;
    private ImageLayer bg;
    private ImageLayer next;
    private ImageLayer backButton;

    public static float x ;
    public static float y ;

    //=======================================================
    // define for character
    private boolean destroy = false;

    public enum Character {
        SWORD, SWORD2, SPEAR, SPEAR2, CROSSBOW
    }

    private Character character;

    private Chis chis;  // use chis character
    private Sword sword;
    private Sword2 sword2;
    private Spear spear;
    private Spear2 spear2;
    private Crossbow crossbow;

    // define item

    //private Arrow arrow;
    private static List<Arrow> arrowList;
    private static List<Arrow> destroyArrow;

    private GroupLayer groupArrow = graphics().createGroupLayer();

    public void addArrow(Arrow arrow){
        arrowList.add(arrow);
    }

    //=======================================================
    // define

    private int i = -1;
    public static HashMap<Object,String> bodies = new HashMap<Object, String>();
    public static HashMap<Object,String> swordList = new HashMap<Object, String>();
    private static int enemies = 5;

    //=======================================================
    // define for world

    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 162;
    private static int height = 18;

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true; // open debug mode
    //private boolean showDebugDraw = false; // close debug mode

    //===================================================================

    public GameScreen2(){}

    public GameScreen2(final ScreenStack ss) {
        this.ss = ss;
        //this.gameScreen2 = new GameScreen2(ss);

        //==================================================================
        // insert bg part

        Image bgImage = assets().getImage("images/bg/level_2.png");
        this.bg = graphics().createImageLayer(bgImage);
        bg.setTranslation(x , y);

        //==================================================================
        // insert clear stage

        Image clearImage = assets().getImage("images/cutscene/Next/clear.png");
        this.next = graphics().createImageLayer(clearImage);

        //==================================================================
        // insert back button

        Image backImage = assets().getImage("images/button/backBut2.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10,405);

        //==================================================================
        // define world

        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        //==================================================================
        // insert ground & left wall in world

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height - 3), new Vec2(width, height - 3));
        ground.createFixture(groundShape, 0.0f);
        bodies.put(ground,"ground");

        EdgeShape groundTop = new EdgeShape();
        groundTop.set(new Vec2(-width, height - 15), new Vec2(width, height - 15));
        ground.createFixture(groundTop, 0.0f);

        EdgeShape left_wall = new EdgeShape();
        left_wall.set(new Vec2(0, 0), new Vec2(0, height));
        ground.createFixture(left_wall, 0.0f);

        //==================================================================
        // insert chis

        chis = new Chis(world, 50f, 360f);
        //bodies.put(chis,"Chis");

        sword = new Sword(world, 1700f, 360f);
        sword2 = new Sword2(world, 700f, 360f);

        spear = new Spear(world, 1750f, 360f);
        spear2 = new Spear2(world, 400f, 360f);
        //bodies.put(spear,"spear_");

        crossbow = new Crossbow(world ,1770f, 360f);
        //bodies.put(crossbow,"crossbow_");

        arrowList     = new ArrayList<Arrow>();
        destroyArrow  = new ArrayList<Arrow>();
    }

    @Override
    public void wasShown(){
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(backButton);

        this.layer.add(chis.layer());

        this.layer.add(sword.layer());
        this.layer.add(sword2.layer());

        this.layer.add(spear.layer());
        this.layer.add(spear2.layer());

        this.layer.add(crossbow.layer());

        this.layer.add(groupArrow);

        //==================================================================
        // back button event

        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
                enemies = 5;
            }
        });

        //==================================================================
        // contract

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                for(Arrow arrow: arrowList){

                    if( a == sword.getBody()&& b == arrow.getBody() ||
                            b == sword.getBody()&& a == arrow.getBody() ){

                        if(sword.getBody().getPosition().x != 25) {
                            character = Character.SWORD;  destroy = true;
                            sword.layer().destroy();      SwordDes = true;
                            destroyArrow.add(arrow);      gameOver();
                        }
                    }

                    if( a == sword2.getBody()&& b == arrow.getBody() ||
                            b == sword2.getBody()&& a == arrow.getBody() ){

                        if(sword2.getBody().getPosition().x != 25) {
                            character = Character.SWORD2;  destroy = true;
                            sword2.layer().destroy();      SwordDes = true;
                            destroyArrow.add(arrow);       gameOver();
                        }
                    }

                    if( a == spear.getBody()&& b == arrow.getBody() ||
                            b == spear.getBody()&& a == arrow.getBody()){

                        if(sword.getBody().getPosition().x != 25) {
                            character = Character.SPEAR;  destroy = true;
                            spear.layer().destroy();      SpearDes = true;
                            destroyArrow.add(arrow);      gameOver();
                        }
                    }

                    if( a == spear2.getBody()&& b == arrow.getBody() ||
                            b == spear2.getBody()&& a == arrow.getBody()){

                        if(sword.getBody().getPosition().x != 25) {
                            character = Character.SPEAR2;  destroy = true;
                            spear2.layer().destroy();      SpearDes2 = true;
                            destroyArrow.add(arrow);      gameOver();
                        }
                    }

                    if( a == crossbow.getBody()&& b == arrow.getBody() ||
                            b == crossbow.getBody()&& a == arrow.getBody()){

                        if(sword.getBody().getPosition().x != 25) {
                            character = Character.CROSSBOW; destroy = true;
                            crossbow.layer().destroy();     CrossDes = true;
                            destroyArrow.add(arrow);        gameOver();
                        }
                    }

                    if( bodies.get(a) == "ground" && b == arrow.getBody() ||
                            bodies.get(b) == "ground" && a == arrow.getBody()){
                        arrow.ContactCheck(contact);
                        destroyArrow.add(arrow);
                    }
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });

        //============================================================
        // debug mode

        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/GameScreen.M_PER_PIXEL),
                    (int)(height/GameScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw= new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit|
                    DebugDraw.e_jointBit|DebugDraw.e_aabbBit);

            debugDraw.setCamera(0,0,1f/GameScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
    }

    int time;
    @Override
    public void update (int delta) {
        super.update(delta);

        chis.update(delta);

        sword.update(delta);
        sword2.update(delta);

        spear.update(delta);
        spear2.update(delta);

        crossbow.update(delta);
        bg.setTranslation(x,0);

        if(destroy == true){
            switch (character){

                case SWORD: world.destroyBody(sword.getBody()); break;
                case SWORD2: world.destroyBody(sword2.getBody()); break;
                case SPEAR: world.destroyBody(spear.getBody()); break;
                case SPEAR2: world.destroyBody(spear2.getBody()); break;
                case CROSSBOW: world.destroyBody(crossbow.getBody()); break;
            }
        }

        //============================================================
        // insert & delete Arrow

        for(Arrow arrow: arrowList){
            arrow.update(delta);
        }

        for(Arrow arrow: arrowList){
            groupArrow.add(arrow.layer());
        }

        // delete arrow body
        while(!destroyArrow.isEmpty()) {
            destroyArrow.get(0).getBody().setActive(false);
            arrowList.get(0).layer().destroy();
            arrowList.remove(0);
            world.destroyBody(destroyArrow.remove(0).getBody());
        }

        //============================================================
        // check enemies

        if(enemies <= 0 ) {
            time += delta;
            this.layer.add(next);
        }

        if(time > 2400 * 1 ) {
            //ss.remove(ss.top());
            //ss.push(new GameScreen3(ss));
            enemies = 5;
        }

        //============================================================
        // screen moving
        if(x < 0 ){ bg.setTranslation(x,0);} else { x = 0; }
        if(x <= -2240) x = -2240;

        world.step(0.033f,10,10);
    }

    @Override
    public void paint(Clock clock){
        super.paint(clock);

        bg.setTranslation(x , y);

        chis.paint(clock);

        sword.paint(clock);
        sword2.paint(clock);

        spear.paint(clock);
        spear2.paint(clock);

        crossbow.paint(clock);

        for(Arrow arrow: arrowList){
            arrow.paint(clock);
        }

        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));

            //============================================================
            // show enemies left
            debugDraw.getCanvas().drawText(
                    String.valueOf("ENEMIES" + " " + " LEFT = " + enemies ),
                    445, 30);

            world.drawDebugData();
        }
    }

    public boolean SwordDes = false;
    public boolean SpearDes = false;
    public boolean SpearDes2 = false;
    public boolean CrossDes = false;

    public void gameOver(){

        float a = sword.getBody().getPosition().x;
        float d = sword2.getBody().getPosition().x;
        float b = spear.getBody().getPosition().x;
        float c = crossbow.getBody().getPosition().x;

        //==================================================
        // sword has destroy

        if(SwordDes == true){
            if(SpearDes == true && CrossDes == true){
                enemies--;
                System.out.println("1");

            } else if (SpearDes == true){

                if (crossbow.side() == true && c < 25) {
                    ss.remove(ss.top());
                    ss.push(new GameOver(ss));
                } else {enemies--;}

                System.out.println("2");

            } else if (CrossDes == true){
                if (spear.side() == true && b < 25){
                    ss.remove(ss.top());
                    ss.push(new GameOver(ss));
                } else {enemies--;}

                System.out.println("3");
            } else if(spear.side() == true && b <= 25 ||
                    crossbow.side() == true && c <= 25){

                ss.remove(ss.top());
                ss.push(new GameOver(ss));
                System.out.println("3.4");

            } else {enemies--;
                System.out.println("4");
            }
        }

        //==================================================
        // spear has destroy

        else if(SpearDes == true){
            if(SwordDes == true && CrossDes == true){
                enemies--;
                System.out.println("5");

            } else if (SwordDes == true){

                if (crossbow.side() == true && c < 25) {
                    ss.remove(ss.top());
                    ss.push(new GameOver(ss));
                } else {enemies--;}

                System.out.println("6");

            } else if (CrossDes == true){
                if (sword.side() == true && a < 25){
                    ss.remove(ss.top());
                    ss.push(new GameOver(ss));
                } else {enemies--;}

                System.out.println("7");

            } else {enemies--;
                System.out.println("8");
            }
        }


        //==================================================
        // crossbow has destroy

        else if(CrossDes == true){
            if(SwordDes == true && SpearDes == true){
                enemies--;
                System.out.println("9");

            } else if (SwordDes == true){

                if (spear.side() == true && b < 25) {
                    ss.remove(ss.top());
                    ss.push(new GameOver(ss));
                } else {enemies--;}

                System.out.println("10");

            } else if (SpearDes == true) {
                if (sword.side() == true && a < 25) {
                    ss.remove(ss.top());
                    ss.push(new GameOver(ss));
                } else {
                    enemies--;
                }

                System.out.println("11");

            }else if(sword.side() == false && a <= 25 ||
                        spear.side() == false && b <= 25) {

                ss.remove(ss.top());
                ss.push(new GameOver(ss));

                System.out.println("13");

            }else {enemies--;
                System.out.println("14");
            }
        }

        if (SpearDes2 == true ){
            if(sword2.side() == true && d <= 25) {
                ss.remove(ss.top());
                ss.push(new GameOver(ss));
            }
        } else {enemies--;}


        System.out.println("side : " + sword.side() + "     " + spear.side() + "     " + crossbow.side());
        System.out.println("destroy : " + SwordDes + "     " + SpearDes + "     " + CrossDes );

    }

}