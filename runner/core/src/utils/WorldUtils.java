package utils;

import box2d.GroundUserData;
import box2d.RunnerUserData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

/**
 * Created by Lancelot on 22.08.2016.
 * <p>
 * World components
 */
public class WorldUtils {

    public static World createWorld() {
        return new World(Constants.WORLD_GRAVITY, true);
    }

    public static Body createGround(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants.GROUND_X, Constants.GROUND_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.GROUND_WIDTH / 2, Constants.GROUND_HEIGHT / 2);
        body.createFixture(shape, Constants.GROUND_DENSITY);

        body.setUserData(new GroundUserData());

        shape.dispose();
        return body;
    }

    //Test class
    public static Body createGround2(World world) {
        //---use maps constants instead of ground one---
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(new Vector2(10f, 2f));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2f, 1f);
        body.createFixture(shape, Constants.GROUND_DENSITY);

        body.setUserData(new GroundUserData());

        shape.dispose();
        return body;
    }

    public static Body createRunner(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.position.set(new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y));

        ChainShape chainShape = new ChainShape();
        Vector2[] vects = new Vector2[4];
        vects[0] = new Vector2(-Constants.RUNNER_WIDTH / 2, -Constants.RUNNER_HEIGHT / 2); //bot left
        vects[1] = new Vector2(-Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2); //top left
        vects[2] = new Vector2(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2); //top right
        vects[3] = new Vector2(Constants.RUNNER_WIDTH / 2, -Constants.RUNNER_HEIGHT / 2); //bot right
        chainShape.createLoop(vects);

//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(Constants.RUNNER_WIDTH / 2, Constants.RUNNER_HEIGHT / 2);

        Body body = world.createBody(bodyDef);
        body.setGravityScale(Constants.RUNNER_GRAVITY_SCALE);
//        body.createFixture(shape, Constants.RUNNER_DENSITY);
        body.createFixture(chainShape, Constants.RUNNER_DENSITY);
        body.resetMassData();
        body.setUserData(new RunnerUserData());

//        shape.dispose();
        chainShape.dispose();

        return body;
    }
}
