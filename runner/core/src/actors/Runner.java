package actors;

import box2d.RunnerUserData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import utils.Constants;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class Runner extends GameActor {

    private boolean jumping;
    private boolean dodging;
    private boolean running;
    private Animation runningAnimation;
    private static final int FRAME_COLS = 16;
    private static final int FRAME_ROWS = 1;

    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;

    float stateTime;
    public Runner(Body body) {
        super(body);
//        TextureAtlas textureAtlas = new TextureAtlas(Constants.CHARACTERS_ATLAS_PATH);
//        System.out.println(Constants.RUNNER_RUNNING_REGION_NAMES.length);
//        TextureRegion[] runningFrames = new TextureRegion[Constants.RUNNER_RUNNING_REGION_NAMES.length];
//        for (int i = 0; i < Constants.RUNNER_RUNNING_REGION_NAMES.length; i++) {
//            String path = Constants.RUNNER_RUNNING_REGION_NAMES[i];
//            runningFrames[i] = textureAtlas.findRegion(path);
//        }
//        runningAnimation = new Animation(0.1f, runningFrames);
//        stateTime = 0f;
        walkSheet = new Texture(Gdx.files.internal(Constants.CHARACTER_ATLAS_PATH));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.05f, walkFrames);
        stateTime = 0f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        batch.draw(walkAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH / 2 - Constants.RUNNER_WIDTH * Constants.SCALE, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                Constants.RUNNER_X * Constants.SCALE, Constants.RUNNER_Y * Constants.SCALE);
    }

    @Override
    public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        getUserData().setRunningPosition(body.getPosition());
        if (running)
            body.setLinearVelocity(getUserData().getLinearVelocity().x, body.getLinearVelocity().y);
    }

    public void runRight() {
        if (!(dodging)) {
            running = true;
            getUserData().setLinearVelocity(new Vector2(10f, body.getLinearVelocity().y));
//            body.setLinearVelocity(10f,0);
        }
    }

    public void runLeft() {
        if (!(dodging)) {
            getUserData().setLinearVelocity(new Vector2(-10f, body.getLinearVelocity().y));
//            body.setLinearVelocity(-10f,0);
            running = true;
        }
    }

    public void stopRunning() {
        running = false;
        if (!dodging)
            body.setLinearVelocity(0, body.getLinearVelocity().y);
    }

    public void jump() {
        if (!(jumping || dodging)) {
            System.out.println("jump");
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(), body.getWorldCenter(), true);
            jumping = true;
        }
    }

    public void landed() {
        jumping = false;
    }

    public void dodge() {
        if (!jumping) {
            body.setTransform(body.getWorldCenter(), getUserData().getDodgeAngle());
            //-------
            //test
//            if(running)
//            {
//                body.setLinearVelocity(0,0);
//                body.applyLinearImpulse(10f,0,body.getWorldCenter().x,body.getWorldCenter().y,true);
//            }
            //endtest
            //-------
            dodging = true;
        }
    }

    public void stopDodge() {
        dodging = false;
        body.setTransform(body.getWorldCenter(), 0f);
    }

    public boolean isDodging() {
        return dodging;
    }
}
