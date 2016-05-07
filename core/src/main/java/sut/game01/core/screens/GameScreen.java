package sut.game01.core.screens;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.*;
import playn.core.util.Clock;
import playn.core.CanvasImage;
import sut.game01.core.characters.Chis;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;


import static playn.core.PlayN.*;

public class GameScreen extends Screen{

  private Chis chis;

  private final ScreenStack ss;
  private final ImageLayer bg;
  private final ImageLayer backButton;

  //=======================================================
  // insert for world

  public static float M_PER_PIXEL = 1 / 26.666667f ;
  private static int width = 24;
  private  static  int height = 18;

  private World world;
  private DebugDrawBox2D debugDraw;
  private boolean showDebugDraw = true;

  //=======================================================

  public GameScreen(final ScreenStack ss) {
    this.ss = ss;

    Image bgImage = assets().getImage("images/bg/main.png");
    this.bg = graphics().createImageLayer(bgImage);

    this.layer.add(bg);

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

    chis = new Chis(560f, 400f);

    mouse().setListener(new Mouse.Adapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position =new Vec2(event.x()*M_PER_PIXEL,event.y()*M_PER_PIXEL);

        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.4f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density=1f;
        fixtureDef.friction=0.1f;
        fixtureDef.restitution=1f;

        body.createFixture(fixtureDef);
        body.setLinearDamping(0.2f);
      }
    });

  }

  @Override
  public void wasShown(){
    super.wasShown();

    this.layer.add(chis.layer());

    this.layer.add(backButton);

    Body ground = world.createBody(new BodyDef());
    EdgeShape groundShape = new EdgeShape();
    groundShape.set(new Vec2(0,height),new Vec2(width,height));
    ground.createFixture(groundShape,0.0f);

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

    if(showDebugDraw){
      debugDraw.getCanvas().clear();
      world.drawDebugData();
    }
  }
}