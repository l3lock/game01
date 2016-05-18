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

public class GameScreen extends Screen{

  //=======================================================
  // define for screen

  private ScreenStack ss;
  private ImageLayer bg;
  private ImageLayer level_1bg;
  private ImageLayer backButton;

  private float x ;
  private float y ;

  //=======================================================
  // define for character
  private boolean destroy = false;

  private Chis chis;  // use chis character
  private Sword sword;
  private Spear spear;
  private Crossbow crossbow;

  // define item

  private Arrow arrow;
  private static List<Arrow> arrowList;

  private GroupLayer groupArrow = graphics().createGroupLayer();

  public void addArrow(Arrow arrow){
    arrowList.add(arrow);
  }

  //=======================================================
  // define

  private int i = -1;
  public static HashMap<Object,String> bodies = new HashMap<Object, String>();
  public static int k = 0;
  public static String debugStringCoin = "";

  //=======================================================
  // define for world

  public static float M_PER_PIXEL = 1 / 26.666667f ;
  private static int width = 162;
  private static int height = 18;

  private World world;
  private DebugDrawBox2D debugDraw;
  private boolean showDebugDraw = true; // open debug mode
  //private boolean showDebugDraw = false; // close debug mode

  //=======================================================

  public GameScreen(){}

  public GameScreen(final ScreenStack ss) {
    this.ss = ss;

    //==================================================================
    // insert bg part

    Image bgImage = assets().getImage("images/bg/level_1.png");
    this.bg = graphics().createImageLayer(bgImage);

    Image bg2 = assets().getImage("images/bg/level_1_bg.png");
    this.level_1bg = graphics().createImageLayer(bg2);
    level_1bg.setTranslation(x , y);

    //==================================================================
    // insert back button

    Image backImage = assets().getImage("images/button/backBut.png");
    this.backButton = graphics().createImageLayer(backImage);
    backButton.setTranslation(10,405);

    backButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.remove(ss.top());
      }
    });

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

    EdgeShape left_wall = new EdgeShape();
    left_wall.set(new Vec2(0, 0), new Vec2(0, height));
    ground.createFixture(left_wall, 0.0f);

    //==================================================================
    // insert chis

    chis = new Chis(world, 45f, 360f);
    sword = new Sword(world, 400f, 360f);
    spear = new Spear(world, 500f, 360f);
    crossbow = new Crossbow(world ,600f, 360f);

    arrowList = new ArrayList<Arrow>();

    //==================================================================
    // contract

    world.setContactListener(new ContactListener() {
      @Override
      public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

    /*    if( contact.getFixtureA().getBody() == arrow.getBody()&&
                contact.getFixtureB().getBody() == spear.getBody()){
          destroy = true;
          spear.layer().destroy();
        }
    */
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
  }

  @Override
  public void wasShown(){
    super.wasShown();
    this.layer.add(level_1bg);
    this.layer.add(bg);
    this.layer.add(backButton);

    this.layer.add(chis.layer());
    this.layer.add(sword.layer());
    this.layer.add(spear.layer());
    this.layer.add(crossbow.layer());

    this.layer.add(groupArrow);

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
  public void update (int delta) {
    super.update(delta);

    chis.update(delta);
    sword.update(delta);
    spear.update(delta);
  /*
    if(destroy == true){
      world.destroyBody(spear.getBody());
    }
  */
    crossbow.update(delta);

    for(Arrow arrow: arrowList){
      arrow.update(delta);
    }

    for(Arrow arrow: arrowList){
      groupArrow.add(arrow.layer());
    }

    world.step(0.033f,10,10);
  }

  @Override
  public void paint(Clock clock){
    super.paint(clock);

    bg.setTranslation(x , y);

    chis.paint(clock);
    sword.paint(clock);
    spear.paint(clock);
    crossbow.paint(clock);

    for(Arrow arrow: arrowList){
      arrow.paint(clock);
    }

    if(showDebugDraw){
      debugDraw.getCanvas().clear();
      debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
      debugDraw.getCanvas().drawText(debugStringCoin, 100f, 100f);
      world.drawDebugData();
    }
  }
}