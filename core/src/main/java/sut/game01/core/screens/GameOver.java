package sut.game01.core.screens;

import static playn.core.PlayN.*;
import static playn.core.PlayN.graphics;

import playn.core.*;

import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

public class GameOver extends Screen{

  private final ScreenStack ss;

  // insert screen
  private final GameScreen gameScreen;
  private final Setting setting;

  // insert image layer
  private ImageLayer bg;
  private ImageLayer backButton;
  private ImageLayer settingButton;

  public GameOver(final ScreenStack ss) {
    this.ss = ss;
    this.gameScreen = new GameScreen(ss);
    this.setting = new Setting(ss);

    Image bgImage = assets().getImage("images/bg/bg4.png");
    this.bg = graphics().createImageLayer(bgImage); 
  
    //==================================================================
    // insert gameOver text
    Image backImage = assets().getImage("images/gameOver.png");
    this.backButton = graphics().createImageLayer(backImage);    
    backButton.setTranslation(90f,90.0f);

    backButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.remove(ss.top());
      }
    });

    //==================================================================
    // insert setting button
    Image settingButtonImage = assets().getImage("images/button/settingBut.png");
    this.settingButton = graphics().createImageLayer(settingButtonImage);
    settingButton.setTranslation(585,10);

    settingButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.push(setting);
      }
    });    

    //==================================================================
  }

  @Override
  public void wasShown(){
    super.wasShown();
    this.layer.add(bg);
    this.layer.add(backButton);
    this.layer.add(settingButton);

    PlayN.keyboard().setListener(new Keyboard.Adapter() {
      @Override
      public void onKeyUp(Keyboard.Event event) {
        if(event.key() == Key.ESCAPE) {
          ss.remove(ss.top());
        }
      }
    });
  }
}