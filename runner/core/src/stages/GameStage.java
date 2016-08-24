package stages;

import actors.Ground;
import actors.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Rectangle;
import utils.BodyUtils;
import utils.WorldUtils;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class GameStage extends Stage implements ContactListener {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    private World world;
    private Ground ground;
    private Runner runner;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private Vector3 touchPoint;

    public GameStage() {
        setUpWorld();
        setupCamera();
        setUpTouchControlAreas();
        renderer = new Box2DDebugRenderer();
    }

    private void setUpTouchControlAreas() {
        touchPoint = new Vector3();
        screenLeftSide = new Rectangle(0, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        screenRightSide = new Rectangle(getCamera().viewportWidth / 2, 0, getCamera().viewportWidth / 2, getCamera().viewportHeight);
        Gdx.input.setInputProcessor(this);
    }

    private void setUpWorld() {
        world = WorldUtils.createWorld();

        //handling contacts
        world.setContactListener(this);
        setUpGround();
        setUpRunner();
    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        //if the runner touch the ground
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
            System.out.println("down");
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }

    private void setUpGround() {
        ground = new Ground(WorldUtils.createGround(world));
        addActor(ground);
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

        //TODO: Implement interpolation
    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }

    //(look at inputProcessor for methodes...)
    @Override
    public boolean keyDown(int keycode) {
//        if(keycode==Input.Keys.UP)
//        {
//            System.out.println("UPP!!!");
//            runner.jump();
//        }else if(keycode==Input.Keys.DOWN)
//        {
//            runner.dodge();
//        }else if(keycode==Input.Keys.RIGHT)
//        {
//            runner.runRight();
//        }

        switch (keycode) {
            case Input.Keys.UP:
                runner.jump();
                break;
            case Input.Keys.DOWN:
                runner.dodge();
                runner.stopRunning();
                break;
            case Input.Keys.RIGHT:
                runner.runRight();
                break;
            case Input.Keys.LEFT:
                runner.runLeft();
                break;
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (runner.isDodging() && keycode == Input.Keys.DOWN) {
            runner.stopDodge();
        }
        if (keycode == Input.Keys.RIGHT || keycode==Input.Keys.LEFT) {
            runner.stopRunning();
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        //actual coordinates
        translateScreenToWorldCoordinates(x, y);
        System.out.println("test");
        if (rightSideTouched(touchPoint.x, touchPoint.y)) {
            runner.jump();
        } else if (leftSideTouched(touchPoint.x, touchPoint.y)) {
            runner.dodge();
        }

        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (runner.isDodging()) {
            runner.stopDodge();
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    private boolean rightSideTouched(float x, float y) {
        return screenRightSide.contains(x, y);
    }

    private boolean leftSideTouched(float x, float y) {
        return screenLeftSide.contains(x, y);
    }

    /**
     * Helper function to get the actual coordinates in my world
     *
     * @param x
     * @param y
     */
    private void translateScreenToWorldCoordinates(int x, int y) {
        getCamera().unproject(touchPoint.set(x, y, 0));
    }

}
