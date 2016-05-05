package sut.game01.core.screen;

import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class Setting extends Screen {
    private ScreenStack ss;

    // insert image layer
    private ImageLayer bg;

    public Setting(final ScreenStack ss){
        this.ss = ss;

        //==================================================================
        // insert bg
        Image bgImage = assets().getImage("images/bg/bg2.png");
        this.bg = graphics().createImageLayer(bgImage);

        //==================================================================
    }

    @Override
    public void wasShown() {
        super.wasShown();

        this.layer.add(bg);
    }
}
