package sut.game01.core.characters;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.screens.GameScreen;
import sut.game01.core.spriteManage.Sprite;
import sut.game01.core.spriteManage.SpriteLoader;
import tripleplay.game.Screen;

public class Sword2 extends Screen{
    private Sprite sprite;
    private int si = 0;
    private boolean hasLoaded = false;

    public enum State {
        L_IDLE,     R_IDLE,
        L_WALK,     R_WALK,
        L_ALERT,    R_ALERT,
        ATTK
    }

    private State state = State.L_WALK;

    private Body body;

    private int e = 0;

    public Sword2(final World world, final float x_px, final float y_px) {

        sprite = SpriteLoader.getSprite("images/characters/swordman/swordman.json");

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

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                45 * GameScreen.M_PER_PIXEL / 2,
                70 * GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;

        fixtureDef.filter.groupIndex = -2;

        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        return body;
    }

    public Layer layer() {
        return sprite.layer();
    }
    public Body getBody(){ return this.body; }

    public void update(int delta) {
        if(hasLoaded == false) return;



        e += delta;
        if(e > 250) {
            switch(state) {
                case R_IDLE:
                    if (!(si >= 0 && si <= 3)){
                        si = 0;
                    }
                    break;

                case L_IDLE:
                    if (!(si >= 5 && si <= 8)){
                        si = 5;
                    }
                    break;

                case R_WALK:
                    if (!(si >= 10 && si <= 12)){
                        si = 10;
                    }
                    break;

                case L_WALK:
                    if (!(si >= 14 && si <= 16)){
                        si = 14;
                    }
                    break;

                case R_ALERT:
                    if (!(si >= 18 && si <= 20)){
                        si = 18;
                    }
                    break;

                case L_ALERT:
                    if (!(si >= 21 && si <= 23)){
                        si = 21;
                    }
                    break;

                case ATTK:
                    if (!(si >= 24 && si <= 25)){
                        si = 24;
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

        switch (state){
            case L_WALK:
                left = true; Walk();
                break;

            case R_WALK:
                left = false; Walk();
                break;
        }

    }

    private boolean left = true;
    private float temp = -1f;
    private void Walk(){
        if(body.getPosition().x  >= 35f) {
            temp = -1f; state = State.L_WALK;
        }
        else if (body.getPosition().x <= 10f) {
            temp = 1f; state = State.R_WALK;
        }
        body.applyForce(new Vec2(3f * temp, 0f), body.getPosition());
    }

    public boolean side(){
        return left;
    }

}
