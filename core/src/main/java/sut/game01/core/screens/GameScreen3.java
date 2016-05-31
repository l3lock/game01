package sut.game01.core.screens;


import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen3 extends Screen{

    //=======================================================
    // define for screen

    private ScreenStack ss;
    private ImageLayer cs1;
    private ImageLayer backButton;

    public GameScreen3(final ScreenStack ss){
        this.ss = ss;

        //==================================================================
        // insert bg
        Image bg = assets().getImage("images/bg/level_3.png");
        this.cs1 = graphics().createImageLayer(bg);

        //==================================================================
        // insert back button

        Image backImage = assets().getImage("images/button/backBut2.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10,405);
    }

    @Override
    public void wasShown() {
        super.wasShown();

        this.layer.add(cs1);
        this.layer.add(backButton);

        //==================================================================
        // back button event

        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
            }
        });

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key() == Key.ESCAPE) {
                    ss.remove(ss.top());
                    ss.push(new GameScreen(ss));
                }
            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);

    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
    }
}
