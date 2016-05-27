package sut.game01.core.item.arrow;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.characters.Chis;
import sut.game01.core.screens.GameScreen;
import sut.game01.core.spriteManage.Sprite;
import sut.game01.core.spriteManage.SpriteLoader;

public class Arrow {
    private Sprite sprite;
    private int e = 0;
    private int si = 0;
    private boolean hasLoaded = false;

    private float x;
    private float y;

    private Body body;

    private char direction;

    public enum State{
        LEFT , RIGHT
    }

    private State state ;

    public Arrow(final World world, final float x_px, final float y_px, final char direction){
        this.x = x_px ; this.y = y_px;
        this.direction = direction;

        sprite = SpriteLoader.getSprite("images/Arrow/arrow_1.json");

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
        bodyDef.position = new Vec2(x, y);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                70 * GameScreen.M_PER_PIXEL / 2,
                1 * GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.35f;

        fixtureDef.filter.groupIndex = -1;

        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        if(direction == 'R'){
            state = State.RIGHT;
            body.applyForce(new Vec2(4f,-1.5f), body.getPosition());
        }else if(direction == 'L') {
            state =State.LEFT;
            body.applyForce(new Vec2(-4f,-1.5f), body.getPosition());
        }

        return body;
    }

    public Layer layer() {
        return sprite.layer();
    }

    public Body getBody(){ return this.body; }

    public void update(int delta) {
        if (hasLoaded == false) return;

        if(direction == 'R'){
            state = State.RIGHT;
        }else {
            state =State.LEFT;
        }

        e += delta;
        if (e > 250) {
            switch (state){
                case LEFT:
                    if (!(si >= 5 && si <= 6)){
                        si = 5;
                    }
                    break;

                case RIGHT:
                    if (!(si >= 0 && si <= 2)){
                        si = 0;
                    }
                    break;
            }

            si++ ;
            sprite.setSprite(si);
            sprite.layer().setTranslation(
                    body.getPosition().x / GameScreen.M_PER_PIXEL,
                    body.getPosition().y / GameScreen.M_PER_PIXEL);
            e = 0;
        }
    }

    public void paint(Clock clock){
        if(!hasLoaded) return;

        sprite.layer().setRotation(body.getAngle());

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL),
                body.getPosition().y / GameScreen.M_PER_PIXEL);

    }

    private boolean checkContact = false;
    public void ContactCheck(Contact contact){
        checkContact = true;
        sprite.layer().setVisible(false);
    }

}
