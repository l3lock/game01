package sut.game01.core.screens;

import playn.core.*;
import sut.game01.core.cutscene.NewGame;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

//=============================================================================================

public class StartScreen extends Screen {

  private ScreenStack ss;  

  private final GameScreen gameScreen;
  private final NewGame newGame;
  private final Setting setting;
  private final LoadScreen loadScreen;

  // insert image layer
  private ImageLayer bg;
  private ImageLayer newButton;
  private ImageLayer loadButton;
  private ImageLayer backButton;
  private ImageLayer settingButton;

  public StartScreen(final ScreenStack ss) {
    float x = 180f, y = 110.0f;

    this.ss = ss;
    this.setting = new Setting(ss);
    this.newGame = new NewGame(ss);
    this.gameScreen = new GameScreen(ss);
    this.loadScreen = new LoadScreen(ss);

    //==================================================================
    // insert bg
    Image bgImage = assets().getImage("images/bg/bg2.png");
    this.bg = graphics().createImageLayer(bgImage);

    //==================================================================    
    // insert new game button
    Image startButtonImage = assets().getImage("images/button/newBut.png");
    this.newButton = graphics().createImageLayer(startButtonImage);
    newButton.setTranslation(x,y);    

    newButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.push(new NewGame(ss));
      }
    });

    //==================================================================    
    // insert load button
    Image loadButtonImage = assets().getImage("images/button/loadBut.png");
    this.loadButton = graphics().createImageLayer(loadButtonImage);
    loadButton.setTranslation(x,y + 90);    

    loadButton.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.push(loadScreen);
      }
    });

    //==================================================================    
    // insert back button
    Image backImage = assets().getImage("images/button/backBut.png");
    this.backButton = graphics().createImageLayer(backImage);
    backButton.setTranslation(x,y + 180);

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
    this.layer.add(newButton);
    this.layer.add(loadButton);
    this.layer.add(backButton);
    this.layer.add(settingButton);

    PlayN.keyboard().setListener(new Keyboard.Adapter() {
      @Override
      public void onKeyUp(Keyboard.Event event) {
        if(event.key() == Key.ENTER) {
          ss.push(new NewGame(ss));
        }
      }
    });
  }

}