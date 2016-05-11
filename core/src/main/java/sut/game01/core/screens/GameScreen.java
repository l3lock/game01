package sut.game01.core.screens;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import playn.core.CanvasImage;
import sut.game01.core.characters.Chis;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;


import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.*;

public class GameScreen extends Screen{

  //=======================================================
  // define for screen

  private final ScreenStack ss;
  private final ImageLayer bg;
  private final ImageLayer backButton;
  private final ImageLayer coin;

  //=======================================================
  // define for character

  private Chis chis;  // use chis character
  private List<Chis> chisList ; //  use chis in list

  //=======================================================
  // define

  private int i = -1;
  public static HashMap<Body,String> bodies = new HashMap<Body, String>();
  public static int k = 0;
  public static int point = 0;
  public static String debugString = "";
  public static String debugStringCoin = "";


  //=======================================================
  // define for world

  public static float M_PER_PIXEL = 1 / 26.666667f ;
  private static int width = 54;
  private static int height = 18;

  private World world;
  private DebugDrawBox2D debugDraw;
  private boolean showDebugDraw = true; // open debug mode
  //private boolean showDebugDraw = false; // close debug mode

  //=======================================================

  public GameScreen(final ScreenStack ss) {
    this.ss = ss;

    Image bgImage = assets().getImage("images/bg/level_1.png");
    this.bg = graphics().createImageLayer(bgImage);

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
    // insert coin
    Image coinImage = assets().getImage("images/coin.png");
    this.coin = graphics().createImageLayer(coinImage);
    coin.setTranslation(283,205);

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

    // insert coin object
    Body coinCircle = world.createBody(new BodyDef());
    CircleShape coinCircleShape = new CircleShape();
    coinCircleShape.setRadius(1.1f);
    coinCircleShape.m_p.set(12f,9f);
    coinCircle.createFixture(coinCircleShape, 0.0f);

    //==================================================================
    // insert chis

    chis = new Chis(world, 40f, 360f);

    world.setContactListener(new ContactListener() {
      @Override
      public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();
        if(bodies.get(a) != null){
          point = point + 10;
          debugString = bodies.get(a) + " contacted with " + bodies.get(b);
          debugStringCoin = "Point : " + point;
          b.applyForce(new Vec2(80f , -100f), b.getPosition());

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
  }

  @Override
  public void wasShown(){
    super.wasShown();

    this.layer.add(bg);
    this.layer.add(coin);
    this.layer.add(backButton);

    this.layer.add(chis.layer());

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
    world.step(0.033f,10,10);
  }

  @Override
  public void paint(Clock clock){
    super.paint(clock);

    chis.paint(clock);

    if(showDebugDraw){
      debugDraw.getCanvas().clear();
      debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
      debugDraw.getCanvas().drawText(debugStringCoin, 100f, 100f);
      world.drawDebugData();
    }
  }
}