package sut.game01.core.screens;

import static playn.core.PlayN.*;
import static playn.core.PlayN.graphics;

import playn.core.*;

import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

//=============================================================================================

public class HomeScreen extends Screen{

  private ScreenStack ss;  

  // insert screen
  private final GameOver gameOver;
  private final GameScreen gameScreen;
  private final StartScreen startScreen;
  private final Setting setting;

  // insert image layer
  private ImageLayer bg;
  private ImageLayer nameGame;
  private ImageLayer startButton;
  private ImageLayer settingButton;

  private Sound music;

  public HomeScreen(final ScreenStack ss) {
    float x = 180f, y = 270.0f;

    this.ss = ss;    
    this.gameOver     = new GameOver(ss);
    this.gameScreen = new GameScreen(ss);
    this.startScreen  = new StartScreen(ss);
    this.setting = new Setting(ss);

    //==================================================================
    // insert bg
    Image bgImage = assets().getImage("images/bg/bg2.png");
    this.bg = graphics().createImageLayer(bgImage);

    //==================================================================
    // insert name text
    Image nameImage = assets().getImage("images/name.png");
    this.nameGame = graphics().createImageLayer(nameImage);
    nameGame.setTranslation(100f,90.0f);

    //==================================================================
    // insert start button
    Image startButtonImage = assets().getImage("images/button/StartBut.png");
    this.startButton = graphics().createImageLayer(startButtonImage);
    startButton.setTranslation(x,y);    

    startButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.push(startScreen);
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
    // insert sound
    music = assets().getSound("sounds/Epilogue");
    music.setVolume(0.75f);    // set volume
  }

  @Override
  public void wasShown(){
    super.wasShown();

    music.play();

    this.layer.add(bg);
    this.layer.add(nameGame);
    this.layer.add(startButton);
    this.layer.add(settingButton);

    //==================================================================
    // if push enter key go to gameScreen
    PlayN.keyboard().setListener(new Keyboard.Adapter() {
      @Override
      public void onKeyUp(Keyboard.Event event) {
        if(event.key() == Key.ENTER) {
          ss.push(startScreen);
        }
      }
    });
  }
}