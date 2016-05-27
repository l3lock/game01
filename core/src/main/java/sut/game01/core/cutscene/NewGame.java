package sut.game01.core.cutscene;

import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.screens.GameScreen;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class NewGame extends Screen {

    //=======================================================
    // define for screen

    private ScreenStack ss;
    private ImageLayer cs1;
    private ImageLayer cs2;
    private ImageLayer cs3;
    private ImageLayer cs4;

    private int time;

    private ImageLayer backButton;

    private World world;

    public NewGame(final ScreenStack ss){
        this.ss = ss;

        //==================================================================
        // insert bg
        Image scene1 = assets().getImage("images/cutscene/NewGame/cutScene_1.png");
        this.cs1 = graphics().createImageLayer(scene1);

        //==================================================================
        // insert bg
        Image scene2 = assets().getImage("images/cutscene/NewGame/cutScene_2.png");
        this.cs2 = graphics().createImageLayer(scene2);

        //==================================================================
        // insert bg
        Image scene3 = assets().getImage("images/cutscene/NewGame/cutScene_3.png");
        this.cs3 = graphics().createImageLayer(scene3);

        //==================================================================
        // insert bg
        Image scene4 = assets().getImage("images/cutscene/NewGame/cutScene_4.png");
        this.cs4 = graphics().createImageLayer(scene4);

    }

    @Override
    public void wasShown() {
        super.wasShown();

        this.layer.add(cs1);

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key() == Key.ESCAPE) {
                    ss.push(new GameScreen(ss));
                }
            }
        });
    }

    @Override
    public void update(int delta) {
        super.update(delta);

        time += delta;

        if(time > 2200 * 1 ){this.layer.add(cs2); }
        if(time > 2200 * 2 ){this.layer.add(cs3); }
        if(time > 2200 * 3 ){this.layer.add(cs4); }
        if(time > 2200 * 4 ){ss.push(new GameScreen(ss)); }

    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
    }
}
