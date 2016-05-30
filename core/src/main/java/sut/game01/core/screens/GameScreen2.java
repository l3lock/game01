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
import sut.game01.core.characters.Chis;
import sut.game01.core.characters.Crossbow;
import sut.game01.core.characters.Spear;
import sut.game01.core.characters.Sword;
import sut.game01.core.item.arrow.Arrow;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.*;

public class GameScreen2 extends Screen{

    //=======================================================
    // define for screen

    private ScreenStack ss;
    private ImageLayer bg;
    private ImageLayer backButton;

    public static float x ;
    public static float y ;

    //=======================================================
    // define for character

    private Chis chis;  // use chis character
    private Sword sword;
    private Spear spear;
    private Crossbow crossbow;

    private static int enemies = 3;

    //=======================================================
    // define for world

    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 162;
    private static int height = 18;

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true; // open debug mode
    //private boolean showDebugDraw = false; // close debug mode

    public GameScreen2(final ScreenStack ss){
        this.ss = ss;

        //==================================================================
        // insert bg part

        Image bgImage = assets().getImage("images/bg/level_2.png");
        this.bg = graphics().createImageLayer(bgImage);

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
        //bodies.put(ground,"ground");

        EdgeShape groundTop = new EdgeShape();
        groundTop.set(new Vec2(-width, height - 15), new Vec2(width, height - 15));
        ground.createFixture(groundTop, 0.0f);

        EdgeShape left_wall = new EdgeShape();
        left_wall.set(new Vec2(0, 0), new Vec2(0, height));
        ground.createFixture(left_wall, 0.0f);

        //==================================================================
        // insert chis
        chis = new Chis(world, 50f, 357f);
    }


    @Override
    public void wasShown() {
        super.wasShown();

        this.layer.add(bg);
        this.layer.add(backButton);

        this.layer.add(chis.layer());

        //==================================================================
        // back button event

        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
            ss.remove(ss.top());
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

    @Override
    public void update(int delta) {
        super.update(delta);

        chis.update(delta);

        //============================================================
        // screen moving
        if(x < 0 ){ bg.setTranslation(x,0);} else { x = 0; }
        // if(x <= -799) x = -799;

        System.out.println(x);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);

        chis.paint(clock);

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
}