package sut.game01.core;

import static playn.core.PlayN.*;
import static playn.core.PlayN.graphics;

import playn.core.Font;
import playn.core.Mouse;
import playn.core.Image;
import playn.core.ImageLayer;

import react.UnitSlot;

import tripleplay.game.UIScreen;
import tripleplay.game.ScreenStack;

import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

//=============================================================================================

public class HomeScreen extends UIScreen{

  public static final Font TITLE_FONT = graphics().createFont("bradley hand itc regular",Font.Style.PLAIN,24);

  private Root root;
  private ScreenStack ss;  
  private final TestScreen testScreen;
  private final GameOver gameOver;

  private ImageLayer startButton;
  private ImageLayer exitButton;

  public HomeScreen( ScreenStack ss) {
    this.ss = ss;
    this.testScreen = new TestScreen(ss);
    this.gameOver   = new GameOver(ss); 

       
  }

  @Override
  public void wasShown(){
    super.wasShown();

    root = iface.createRoot(
      AxisLayout.vertical().gap(15),
      SimpleStyles.newSheet(), this.layer);

    root.addStyles(Style.BACKGROUND
      .is(Background.image(assets().getImage("images/main.png"))    
    ));
    
    root.setSize(width(), height());    
    
    //===============================================================

    Image startImage = assets().getImageSync("images/startBut.png");
    this.startButton = graphics().createImageLayer(startImage);

    root.add(new ImageButton(startImage).onClick(new UnitSlot() {
      public void onEmit() {
        ss.push(testScreen);
      }
    }));

    //===============================================================

    Image exitImage = assets().getImageSync("images/exitBut.png");
    this.exitButton = graphics().createImageLayer(exitImage);

    root.add(new ImageButton(exitImage).onClick(new UnitSlot() {
      public void onEmit() {
        ss.push(testScreen);
      }
    }));
   
  }
}