package sut.game01.core.characters;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.item.arrow.Arrow;
import sut.game01.core.screens.GameScreen;
import sut.game01.core.spriteManage.Sprite;
import sut.game01.core.spriteManage.SpriteLoader;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.List;

public class Chis extends Screen {

    public GameScreen game = new GameScreen();

    private Sprite sprite;
    private int si = 0;
    private boolean hasLoaded = false;

    private List<Arrow> arrowList;

    public enum State {
        L_IDLE,     R_IDLE,
        L_WALK,     R_WALK,
        L_ATTK,     R_ATTK,
    }

    private State state = State.R_IDLE;

    private Body body;
    private World world;

    private int e = 0;

    public Chis(final World world, final float x_px, final float y_px) {
        this.world = world;
        arrowList = new ArrayList<Arrow>();

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

        PolygonShape shape = new PolygonShape();
            shape.setAsBox(
                    45 * GameScreen.M_PER_PIXEL / 2,
                    70 * GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.1f;

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

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyUp(Keyboard.Event event) {
                switch (event.key()){
                    case LEFT:
                        left = true;
                        state = State.L_IDLE; break;
                    case RIGHT:
                        left = false;
                        state = State.R_IDLE; break;
                    case UP:
                        if(left == true) { state = State.L_IDLE; }
                        else { state = State.R_IDLE; }
                        Jump(left);
                        break;
                    case SPACE:
                        if(left == true) { state = State.L_IDLE; }
                        else { state = State.R_IDLE; }
                        break;
                }
            }

            @Override
            public void onKeyDown(Keyboard.Event event) {
                switch (event.key()){
                    case LEFT:
                        left = true;
                        state = State.L_WALK; break;
                    case RIGHT:
                        left = false;
                        state = State.R_WALK; break;
                    case DOWN:
                        if(left == true) { state = State.L_IDLE; }
                        else { state = State.R_IDLE; }
                        break;
                    case SPACE:
                        Arrow arrow_1;
                        if(left == true) {
                            state = State.L_ATTK;
                            arrow_1 = new Arrow(world,
                                    body.getPosition().x / GameScreen.M_PER_PIXEL - 55,
                                    body.getPosition().y / GameScreen.M_PER_PIXEL,
                                    'L');
                            game.addArrow(arrow_1);
                        } else {
                            state = State.R_ATTK;
                            arrow_1 = new Arrow(world,
                                    body.getPosition().x / GameScreen.M_PER_PIXEL + 55,
                                    body.getPosition().y / GameScreen.M_PER_PIXEL,
                                    'R');
                            game.addArrow(arrow_1);
                        }
                        break;
                }
            }
        });

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

                case R_ATTK:
                    if (!(si >= 18 && si <= 19)){
                        si = 18;
                    }
                    break;

                case L_ATTK:
                    if (!(si >= 21 && si <= 22)){
                        si = 21;
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
                left = true; Walk(left);
                break;

            case R_WALK:
                left = false; Walk(left);
                break;
        }
    }

    private boolean left = false;
    private void Jump(boolean left){
        if(left == true) {
            body.applyForce(new Vec2(-20f, -400f), body.getPosition());
        }else{
            body.applyForce(new Vec2(20f, -400f), body.getPosition());
        }
    }

    private void Walk(boolean left){
        if(left == true) {
            body.applyForce(new Vec2(-3f, 0f), body.getPosition());
        }else{
            body.applyForce(new Vec2(3f, 0f), body.getPosition());
        }
    }
}
