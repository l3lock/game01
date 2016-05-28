package sut.game01.core.screens;

import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class Setting extends Screen {
    private ScreenStack ss;

    private boolean click = false;

    // insert image layer
    private ImageLayer bg;
    private ImageLayer backButton;
    private ImageLayer sound;
    private ImageLayer sound2;

    public Setting(final ScreenStack ss){
        this.ss = ss;

        //==================================================================
        // insert bg
        Image bgImage = assets().getImage("images/bg/settingBG.png");
        this.bg = graphics().createImageLayer(bgImage);

        //==================================================================
        // insert back button
        Image backImage = assets().getImage("images/button/backBut2.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(30,380);

        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
            }
        });

        //==================================================================
        // insert sound button
        Image soundImage = assets().getImage("images/button/sound.png");
        this.sound = graphics().createImageLayer(soundImage);
        sound.setTranslation(360,320);

        //==================================================================
        // insert sound button
        Image sound2Image = assets().getImage("images/button/sound2.png");
        this.sound2 = graphics().createImageLayer(sound2Image);
        sound2.setTranslation(360,320);

        sound.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                click = true;
            }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();

        this.layer.add(bg);
        this.layer.add(backButton);
        this.layer.add(sound);
    }

    private int click1 = 0;
    @Override
    public void update(int delta) {
        super.update(delta);

        if(click){
            if(click1 % 2 == 0) {
                this.layer.add(sound2);
            }else {
                this.layer.remove(sound2);
            }
            click = false;
            click1++;
        }

    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
    }
}
