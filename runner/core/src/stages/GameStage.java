package stages;

import actors.*;
import actors.HUD.*;
import box2d.DeadZoneUserData;
import box2d.EndLevelUserData;
import box2d.GroundUserData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.RunnerGame;
import utils.BodyUtils;
import utils.Constants;
import utils.WorldUtils;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class GameStage extends Stage implements ContactListener {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = Constants.APP_WIDTH;
    private static final int VIEWPORT_HEIGHT = Constants.APP_HEIGHT;

    private boolean debug = true;


    private World world;
    private Ground ground;
    private Ground g2; //Test
    private Runner runner;
    private Background background;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    private Rectangle screenRightSide;
    private Rectangle screenLeftSide;

    private TiledMap tileMap;
    private int tileMapWidth;
    private int tileMapHeight;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmRenderer;

    private TextEndLevel textEndLevel;
    private ButtonPause pauseButton;
    private TextScore textScore;
    private RunnerGame game;
    private Vector3 touchPoint;

    public GameStage() {
        super(new ScalingViewport(Scaling.stretch, VIEWPORT_WIDTH, VIEWPORT_HEIGHT,
                new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT)));
        game = RunnerGame.getINSTANCE();
        setUpWorld();
        setupCamera();
        setUpTouchControlAreas();
        renderer = new Box2DDebugRenderer();
    }

    //useless
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
        //setUpGround();
        setUpBackground();
        setUpRunner();
        setUpPause();
        setUpScore();
        createWalls();
    }

    private void setUpBackground() {
        background = new Background();
        addActor(background);
    }

    private void createWalls() {
        tileMap = new TmxMapLoader().load("core\\assets\\map\\level"+ game.getCurrentLevel()+".tmx");
        tileMapWidth = tileMap.getProperties().get("width", Integer.class);
        tileMapHeight = tileMap.getProperties().get("height", Integer.class);
        tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
        tmRenderer = new OrthogonalTiledMapRenderer(tileMap, 1 / Constants.SCALE);
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("obstacle");
        createBlocks(layer);

        layer = (TiledMapTileLayer) tileMap.getLayers().get("invisible");
        createInvisible(layer);
    }

    private void createInvisible(TiledMapTileLayer layer) {
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;
                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyType.StaticBody;
                bdef.position.set((col + 0.5f), (row + 0.5f));

                Body body = world.createBody(bdef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.5f, 0.5f);
                body.createFixture(shape, Constants.GROUND_DENSITY);
                switch (cell.getTile().getId()) {
                    case 1:
                        body.setUserData(new DeadZoneUserData());
                        addActor(new DeadZone(body));
                        break;
                    case 2:
                        body.setUserData(new EndLevelUserData());
                        addActor(new EndLevel(body));
                        break;
                }
                shape.dispose();
                //cs.dispose();

            }
        }
    }

    private void createBlocks(TiledMapTileLayer layer) {

        // tile size
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyType.StaticBody;
                bdef.position.set((col + 0.5f), (row + 0.5f));

                Body body = world.createBody(bdef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.5f, 0.5f);

                body.createFixture(shape, Constants.GROUND_DENSITY);
                body.setUserData(new GroundUserData());
                shape.dispose();
                addActor(new Ground(body));
                //cs.dispose();

            }
        }

    }

    @Override
    public void beginContact(Contact contact) {
        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        //if the runner touch the ground
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsGround(b)) ||
                (BodyUtils.bodyIsGround(a) && BodyUtils.bodyIsRunner(b))) {

            //avoid the jump when runner is falling 
            if (contact.getWorldManifold().getNormal().x == 0 && !runner.isRising()) {
                runner.landed();
            }
        }// contact between runner and deadzone
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsDeadZone(b)) ||
                (BodyUtils.bodyIsDeadZone(a) && BodyUtils.bodyIsRunner(b))) {
            runner.landed();
            game.reset();
            //setUpWorld():
//            runner.getUserData().setRunningPosition(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));
            System.out.println("perdu");
        }
        if ((BodyUtils.bodyIsRunner(a) && BodyUtils.bodyIsEndLevel(b)) ||
                (BodyUtils.bodyIsEndLevel(a) && BodyUtils.bodyIsRunner(b))) {
            if (contact.getWorldManifold().getNormal().x == 0){
                game.setEndLevel();
                runner.landed();
                setUpEndLevel();
                System.out.println("Fin du niveau");
            }

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
        //avoid the runner to run when he's against a wall
        if (runner.getRunnerDir() == Runner.direction.RIGHT && contact.getWorldManifold().getNormal().x == 1) {
            runner.stopRunning();
        } else if (runner.getRunnerDir() == Runner.direction.LEFT && contact.getWorldManifold().getNormal().x == -1) {
            runner.stopRunning();
        }
    }

    private void setUpRunner() {
        runner = new Runner(WorldUtils.createRunner(world));
        addActor(runner);
    }

    private void setUpGround() {
        ground = new Ground(WorldUtils.createGround(world));

        //test------------
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.GROUND_X, 5f));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2f, 2f);
        body.createFixture(shape, Constants.GROUND_DENSITY);

        body.setUserData(new GroundUserData());

        shape.dispose();
        addActor(new Ground(body));
        //test end---------------

        addActor(ground);

        Ground g3 = new Ground(WorldUtils.createGround2(world));
        addActor(g3);

    }

    private void setupCamera() {
        camera = new OrthographicCamera(scale(VIEWPORT_WIDTH), scale(VIEWPORT_HEIGHT));
//        camera.setToOrtho(false,Constants.APP_WIDTH,Constants.APP_HEIGHT);
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

        //TODO: Implement interpolation
    }

    @Override
    public void draw() {
        super.draw();
//        System.out.println(runner.getUserData().getLinearVelocity());
        camera.position.set(runner.getUserData().getRunningPosition().x, runner.getUserData().getRunningPosition().y, 0f);
        camera.update();
        textScore.update();
        background.setSpeed(runner.getUserData().getLinearVelocity().x);
        // System.out.println(runner.getUserData().getLinearVelocity().x);
        tmRenderer.setView(camera);
//        TiledMapTileLayer layer;
//        layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
//        tmRenderer.renderTileLayer(layer);
        tmRenderer.render();

        if (debug) {
            renderer.render(world, camera.combined);
        }

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
        if (!game.getState()) {
            switch (keycode) {
                case Input.Keys.UP:
                    runner.jump();
                    break;
                case Input.Keys.DOWN:
                    runner.dodge();
                    runner.stopRunning();
                    break;
                case Input.Keys.RIGHT:
                    //camera.position.x+=1f;
                    runner.runRight();
                    break;
                case Input.Keys.LEFT:
                    //camera.position.x-=1f;
                    runner.runLeft();
                    break;
            }
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (runner.isDodging() && keycode == Input.Keys.DOWN) {
            runner.stopDodge();
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.LEFT) {
            runner.stopRunning();
        }
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

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
