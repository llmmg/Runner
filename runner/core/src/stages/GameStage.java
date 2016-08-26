package stages;

import actors.Background;
import actors.Ground;
import actors.Runner;
import box2d.GroundUserData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.math.Rectangle;
import utils.BodyUtils;
import utils.Constants;
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
    private Ground g2; //Test
    private Runner runner;

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
        //setUpGround();
        setUpRunner();
        createWalls();

        addActor(new Background());
    }

    private void createWalls() {
        tileMap = new TmxMapLoader().load("core\\assets\\map\\level.tmx");
        tileMapWidth = tileMap.getProperties().get("width", Integer.class);
        tileMapHeight=tileMap.getProperties().get("height", Integer.class);
        tileSize=tileMap.getProperties().get("tilewidth",Integer.class);
        tmRenderer = new OrthogonalTiledMapRenderer(tileMap,1/32f);
        TiledMapTileLayer layer;
        layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
        createBlocks(layer);
        layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
        createBlocks(layer);
        layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
        createBlocks(layer);
    }

    private void createBlocks(TiledMapTileLayer layer) {

        // tile size
        float ts = layer.getTileWidth();

        // go through all cells in layer
        for(int row = 0; row < layer.getHeight(); row++) {
            for(int col = 0; col < layer.getWidth(); col++) {

                // get cell
                Cell cell = layer.getCell(col, row);

                // check that there is a cell
                if(cell == null) continue;
                if(cell.getTile() == null) continue;

                // create body from cell
                BodyDef bdef = new BodyDef();
                bdef.type = BodyType.StaticBody;
                bdef.position.set(col+0.5f,row+0.5f);

                Body body = world.createBody(bdef);
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(0.5f,0.5f);

                body.createFixture(shape,Constants.GROUND_DENSITY);
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
            runner.landed();
            System.out.println("down");
        }
    }

    @Override
    public void endContact(Contact contact) {
//        System.out.println("up");
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

        Ground g3= new Ground(WorldUtils.createGround2(world));
        addActor(g3);

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
        tmRenderer.setView(camera);
//        TiledMapTileLayer layer;
//        layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
//        tmRenderer.renderTileLayer(layer);
        tmRenderer.render();
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
