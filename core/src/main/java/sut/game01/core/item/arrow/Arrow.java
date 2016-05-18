package sut.game01.core.item.arrow;

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
    private int contractCheck;
    private boolean contacted;

    private Body other;
    private char direction;

    private int checkArrow = 0;
    private boolean check = true;
    private boolean checkActive = true;

    public enum State{
        IDLE
    }

    private State state = State.IDLE;

    public Arrow(final World world, final float x_px, final float y_px, final char direction){
        this.x = x_px ; this.y = y_px; this.direction = direction;

        sprite = SpriteLoader.getSprite("images/characters/Arrow/arrow_1.json");

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
                20 * GameScreen.M_PER_PIXEL / 2,
                20 * GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.1f;

        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        if(direction == 'L') body.applyForce(new Vec2(1500f,0f), body.getPosition());
        else if(direction == 'R') body.applyForce(new Vec2(-1500f,0f), body.getPosition());

        return body;
    }

    public Layer layer() {
        return sprite.layer();
    }

    public Body getBody(){ return this.body; }

    public void update(int delta) {

        if (hasLoaded == false) return;

        checkArrow += delta;
        e += delta;

        if (e > 150) {
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

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL),
                body.getPosition().y / GameScreen.M_PER_PIXEL);

    }

    public void contract(Contact contact, Chis chis){
        contacted = true;
        contractCheck = 0;

        if (contact.getFixtureA().getBody() == body){
            other = contact.getFixtureB().getBody();
        } else {
            other = contact.getFixtureA().getBody();
        }
    }

    public void force(){
        body.applyForce(new Vec2(1500f,0f), body.getPosition());
    }
}
