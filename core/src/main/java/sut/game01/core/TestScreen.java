package sut.game01.core;

import static playn.core.PlayN.*;
import static playn.core.PlayN.graphics;

import playn.core.Font;
import playn.core.Mouse;
import playn.core.Image;
import playn.core.ImageLayer;

import react.UnitSlot;

import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;

public class TestScreen extends Screen{

  public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,24);

  private final ScreenStack ss;
  private final ImageLayer bg;
  private final ImageLayer backButton;

  public TestScreen(final ScreenStack ss) {
    this.ss = ss;

    Image bgImage = assets().getImage("images/main.png");
    this.bg = graphics().createImageLayer(bgImage);  

    //==================================================================  
  
    Image backImage = assets().getImage("images/backBut.png");
    this.backButton = graphics().createImageLayer(backImage);
    backButton.setTranslation(10,405);

    backButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.remove(ss.top());
      }
    });

  }

  @Override
  public void wasShown(){
    super.wasShown();
    this.layer.add(bg);
    this.layer.add(backButton);
  }
}