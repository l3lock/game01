package sut.game01.core.screens;

import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class LoadScreen extends Screen{

    private ScreenStack ss;

    private final Setting setting;
    private final GameScreen gameScreen;

    private ImageLayer settingButton;
    private ImageLayer backButton;
    private ImageLayer bg;
    private ImageLayer lv1;
    private ImageLayer lv2;
    private ImageLayer lv3;

    public LoadScreen(final ScreenStack ss){

        float x = 23f, y = 110.0f;

        this.setting    = new Setting(ss);
        this.gameScreen = new GameScreen(ss);

        //==================================================================
        // insert bg
        Image bgImage = assets().getImage("images/bg/map.png");
        this.bg = graphics().createImageLayer(bgImage);

        //==================================================================
        // insert back button
        Image backImage = assets().getImage("images/button/backBut2.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(x,y + 290);

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
        // insert setting button
        Image lvImage1 = assets().getImage("images/button/num_01.png");
        this.lv1 = graphics().createImageLayer(lvImage1);
        lv1.setTranslation(345,340);

        lv1.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(new GameScreen(ss));
            }
        });

        //==================================================================
        // insert setting button
        Image lvImage2 = assets().getImage("images/button/num_02.png");
        this.lv2 = graphics().createImageLayer(lvImage2);
        lv2.setTranslation(250,215);

        lv2.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(new GameScreen2(ss));
            }
        });

        //==================================================================
        // insert setting button
        Image lvImage3 = assets().getImage("images/button/num_03.png");
        this.lv3 = graphics().createImageLayer(lvImage3);
        lv3.setTranslation(385,100);
/*
        lv3.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(new GameScreen3(ss));
            }
        });
*/
    }

    @Override
    public void wasShown(){
        super.wasShown();

        this.layer.add(bg);
        this.layer.add(lv1);
        this.layer.add(lv2);
        this.layer.add(lv3);
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
