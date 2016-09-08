package stages;

import actors.*;
import actors.HUD.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.RunnerGame;
import utils.BodyUtils;
import utils.Constants;
import utils.WorldUtils;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class GameStage extends Stage implements ContactListener {

    // This is our viewport measurements while working with the debug renderer (was 20,13)
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private boolean debug = false;

    private World world;
    private Ground ground;
    private Runner runner;
    private Background background;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private TextEndLevel textEndLevel;
    private ButtonPause pauseButton;
    private TextScore textScore;
    private RunnerGame game;

    public GameStage() {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        game = RunnerGame.getINSTANCE();
        setUpWorld();
        setupCamera();

        //inputs
        Gdx.input.setInputProcessor(this);
        renderer = new Box2DDebugRenderer();
    }

    /**
     * Initialize world.
     * Call setUp functions
     */
    private void setUpWorld() {
        world = WorldUtils.createWorld();

        //handling contacts
        world.setContactListener(this);
        setUpBackground();
        setUpRunner();
        setUpPause();
        setUpScore();
        createWalls();
    }

    /**
     * Initialize background and add it in the stage
     */
    private void setUpBackground() {
        background = new Background();
        addActor(background);
    }

    /**
     * Setup runner and add it to the stage.
     */
    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }

    private void setupCamera() {
        camera = new OrthographicCamera(scale(VIEWPORT_WIDTH), scale(VIEWPORT_HEIGHT));
        camera.position.set(scale(VIEWPORT_WIDTH) / 2, scale(VIEWPORT_HEIGHT) / 2, 0f);
        camera.update();
    }

    private void setUpPause() {
        pauseButton = new ButtonPause();
        addActor(pauseButton.getButtonPause());
    }

    private void setUpScore() {
        textScore = new TextScore();
        addActor(textScore.getTextScore());
    }

    private void setUpEndLevel() {
        textEndLevel = new TextEndLevel();
        textEndLevel.showTextEndLevel(textScore.getTime());
        addActor(textEndLevel.getTextEndLevel());
        textEndLevel.setVisible(true);
    }

    public float scale(float valueToBeScaled) {
        return valueToBeScaled / Constants.SCALE;
    }

    /**
     * Import the tile map from .tmx file
     */
    private void createWalls() {
        tileMap = new TmxMapLoader().load(String.format(Constants.MAP_TMX_PATH, game.getCurrentLevel()));
        tileMapWidth = tileMap.getProperties().get("width", Integer.class);
        tileMapHeight = tileMap.getProperties().get("height", Integer.class);
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        tmRenderer = new OrthogonalTiledMapRenderer(tileMap, 1 / Constants.SCALE);
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("obstacle");
        WorldUtils.createBlocks(layer, this, world);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("invisible");
        WorldUtils.createInvisibleCells(layer, this, world);
    }

    /**
     * libgdx beginContact function:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/physics/box2d/ContactListener.html#beginContact-com.badlogic.gdx.physics.box2d.Contact-">beginContact</a>
     *
     * @param contact <p>
     *                Detect collisions between the runner and other body like ground, endLevel or deadZone.
     *                Avoid the multi-jump for the runner.
     *                </p>
     */
    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        float normal = contact.getWorldManifold().getNormal().x;
        //if the runner touch the ground
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {

            //avoid the jump when runner is falling 
            if ((normal <= 0.2 && normal >= -0.2) && !runner.isRising()) {
                runner.landed();
            }
//            System.out.println(normal);
        }
        // contact between runner and deadzone
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsDeadZone(b)) ||
                (BodyUtils.bodyIsDeadZone(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
            game.reset();
//            System.out.println("perdu");
        }
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEndLevel(b)) ||
                (BodyUtils.bodyIsEndLevel(a) && BodyUtils.bodyIsRunner(b))) {
            if (normal == 0) {
                game.setEndLevel();
                runner.landed();
                setUpEndLevel();
//                System.out.println("Fin du niveau");
            }
        }
    }

    /**
     * libgdx endContact function:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/physics/box2d/ContactListener.html#endContact-com.badlogic.gdx.physics.box2d.Contact-">endContact</a>
     *
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
    }

    /**
     * libgdx preSolve function:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/physics/box2d/ContactListener.html#preSolve-com.badlogic.gdx.physics.box2d.Contact-com.badlogic.gdx.physics.box2d.Manifold-">preSolve</a>
     *
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    /**
     * libgdx postSolve function:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/physics/box2d/ContactListener.html#postSolve-com.badlogic.gdx.physics.box2d.Contact-com.badlogic.gdx.physics.box2d.ContactImpulse-">postSolve</a>
     *
     * @param contact
     * @param impulse <p> Used to avoid the runner to keep running when he's against a wall
     *                </p>
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
//        System.out.println("postSolve");
        //avoid the runner to run when he's against a wall
        if (runner.getRunnerDir() == Runner.direction.RIGHT && contact.getWorldManifold().getNormal().x == 1) {
            runner.stopRunning();
        } else if (runner.getRunnerDir() == Runner.direction.LEFT && contact.getWorldManifold().getNormal().x == -1) {
            runner.stopRunning();
        }
    }

    /**
     * libgdx act function:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#act--">act</a>
     *
     * @param delta <p>
     *              compute time steps
     *              </p>
     */
    @Override
    public void act(float delta) {
        if (!game.getState()) {
            delta = Gdx.graphics.getRawDeltaTime();
        } else {
            delta = 0;
        }
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    /**
     * libgdx draw function
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#draw--">draw</a>
     * <p>
     *     Render camera, text score, background and tiledmap
     * </p>
     */
    @Override
    public void draw() {
        super.draw();
        camera.position.set(runner.getUserData().getRunningPosition().x, runner.getUserData().getRunningPosition().y, 0f);
        camera.update();

        textScore.update();
        background.setSpeed(runner.getUserData().getLinearVelocity().x);
        tmRenderer.setView(camera);
        tmRenderer.render();


        if (debug) {
            renderer.render(world, camera.combined);
        }

    }

    /**
     * libgdx keyDown funciton:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#keyDown-int-">keyDown</a>
     *
     * <p>
     *     Read the key tiped. <br>
     *         W key call runner.jump().
     *         S key call runner.dodge() and runner.stopRunning().
     *         D key call runner.runRight().
     *         A key call runner.runLeft().
     * </p>
     */
    @Override
    public boolean keyDown(int keycode) {
        if (!game.getState()) {
            switch (keycode) {
                case Input.Keys.W:
                    runner.jump();
                    break;
                case Input.Keys.S:
                    runner.dodge();
                    runner.stopRunning();
                    break;
                case Input.Keys.D:
                    runner.runRight();
                    break;
                case Input.Keys.A:
                    runner.runLeft();
                    break;
            }
        }
        return super.keyDown(keycode);
    }

    /**
     * libgdx keyUp function:
     * <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html#keyUp-int-">keyUp</a>
     *
     * @param keycode
     * @return <p>
     * Stop actions when key are released like running or dodging...
     * </p>
     */
    @Override
    public boolean keyUp(int keycode) {
        if (runner.isDodging() && keycode == Input.Keys.S) {
            runner.stopDodge();
        }
        if (keycode == Input.Keys.D || keycode == Input.Keys.A) {
            runner.stopRunning();
        }
        return super.keyUp(keycode);
    }

}
