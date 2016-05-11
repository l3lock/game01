package sut.game01.core.characters;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screens.GameScreen;
import sut.game01.core.spriteManage.Sprite;
import sut.game01.core.spriteManage.SpriteLoader;
import tripleplay.game.Screen;

public class Chis extends Screen{
    private Sprite sprite;
    private int si = 0;
    private boolean hasLoaded = false;

    public enum State {
        ALERT, ATTK, IDLE, L_WALK, R_WALK
    }

    private State state = State.IDLE;

    private Body body;

    private int e = 0;

    public Chis(final World world, final float x_px, final float y_px) {

        sprite = SpriteLoader.getSprite("images/characters/chis/chis.json");
        sprite.addCallback(new Callback<Sprite>() {

            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(si);
                sprite.layer().setOrigin(
                        sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x_px, y_px + 13f);

                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL * x_px,
                        GameScreen.M_PER_PIXEL * y_px);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });
    }

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        GameScreen.bodies.put(body, "test_" + GameScreen.k);
        GameScreen.k++ ;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(54 * GameScreen.M_PER_PIXEL / 2,
                sprite.layer().height() * GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;

        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        return body;
    }

    public Layer layer() {
        return sprite.layer();
    }

    public void update(int delta) {
        if(hasLoaded == false) return;

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyUp(Keyboard.Event event) {
                switch (event.key()){
                    case LEFT:
                        state = State.IDLE; break;
                    case RIGHT:
                        state = State.IDLE; break;
                    case SPACE:
                        state = State.IDLE;
                        body.applyForce(new Vec2(-10f, -800f), body.getPosition());
                        break;
                }
            }

            @Override
            public void onKeyDown(Keyboard.Event event) {
                switch (event.key()){
                    case LEFT:
                        state = State.L_WALK; break;
                    case RIGHT:
                        state = State.R_WALK; break;
                }
            }
        });

        e += delta;
        if(e > 250) {
            switch(state) {
                case ALERT:
                    if (!(si >= 0 && si <= 4)){
                        si = 0;
                    }
                    break;

                case ATTK:
                    if (!(si >= 5 && si <= 7)){
                        si = 5;
                    }
                    break;

                case IDLE:
                    if (!(si >= 8 && si <= 12)){
                        si = 8;
                    }
                    break;

                case L_WALK:
                    if (!(si >= 12 && si <= 15)){
                        si = 12;
                    }
                    break;

                case R_WALK:
                    if (!(si >= 12 && si <= 15)){
                        si = 12;
                    }
                    break;
            }
            si ++ ;
            sprite.setSprite(si);
            e = 0;
        }
    }

    @Override
    public void paint(Clock clock) {
        if (!hasLoaded) return;

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL) - 10,
                body.getPosition().y / GameScreen.M_PER_PIXEL);

        sprite.layer().setRotation(body.getAngle());

        switch (state){
            case L_WALK:
                body.applyForce(new Vec2(-5f, 0f), body.getPosition());
                break;

            case R_WALK:
                body.applyForce(new Vec2(5f, 0f), body.getPosition());
                break;
        }
    }

}
