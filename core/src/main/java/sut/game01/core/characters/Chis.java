package sut.game01.core.characters;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.spriteManage.Sprite;
import sut.game01.core.spriteManage.SpriteLoader;
import tripleplay.game.Screen;

public class Chis extends Screen{
    private Sprite sprite;
    private int si = 0;
    private boolean hasLoaded = false;

    private float x;
    private float y;

    public enum State {
        ALERT, ATTK, IDLE, WALK, LWALK
    }

    private State state = State.IDLE;

    private int e = 0;
    private int offset = 0;

    public Chis(final float x, final float y) {

        sprite = SpriteLoader.getSprite("images/characters/chis/chis.json");
        sprite.addCallback(new Callback<Sprite>() {

            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(si);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
    }

    public Layer layer() {
        return sprite.layer();
    }

    public void update(int delta) {
        if(hasLoaded == false) return;

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key() == Key.SPACE) {
                    switch(state) {
                        case IDLE: state = State.ATTK; break;
                        case ATTK: state = State.WALK; break;
                        case WALK: state = State.IDLE; break;
                    }
                }
            }
        });

        e += delta;
        if(e > 250) {
            switch(state) {
                case ALERT: offset = 0;  break;
                case IDLE: 	offset = 5; break;
                case WALK:  offset = 8; break;
                case ATTK:  offset = 12; break;
            }

            si = offset +((si + 1) % 6);
            sprite.setSprite(si);
            e = 0;
        }
    }

    @Override
    public void paint(Clock clock) {
        switch (state){
            case WALK:
                x += 5f;
                sprite.layer().setTranslation(x, y + 13f);
                break;
            case LWALK:
                x -= 5f;
                sprite.layer().setTranslation(x, y + 13f);
                break;
        }
    }
}
