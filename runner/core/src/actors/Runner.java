package actors;

import box2d.RunnerUserData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import utils.Constants;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class Runner extends GameActor {

    private boolean jumping;
    private boolean dodging;
    private boolean running;

    public enum direction {
        LEFT,
        RIGHT
    }

    private Animation walkAnimation;
    private Animation idleAnimation;
    private Animation jumpAnimation;
    private Animation currentAnimation;

    private Texture spriteSheet;
    private TextureRegion[] walkFrames;
    private TextureRegion[] idleFrames;
    private TextureRegion[] jumpFrames;

    private int imgWalk = 12;
    private int imgIdle = 3;
    private int imgJump = 3;
    private float spriteWidth;

    float stateTime;

    //runner direction
    private direction runnerDir;

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


        walkFrames = new TextureRegion[imgWalk];
        createAnimation(walkFrames, Constants.CHARACTER_RUN_PATH, imgWalk);
        walkAnimation = new Animation(0.05f, walkFrames);

        idleFrames = new TextureRegion[imgIdle];
        createAnimation(idleFrames, Constants.CHARACTER_IDLE_PATH, imgIdle);
        idleAnimation = new Animation(0.1f, idleFrames);

        jumpFrames = new TextureRegion[imgJump];
        createAnimation(jumpFrames, Constants.CHARACTER_JUMP_PATH, imgJump);
        jumpAnimation = new Animation(0.1f, jumpFrames);

    }

    public void createAnimation(TextureRegion[] texReg, String name, int nbImg) {
        spriteSheet = new Texture(Gdx.files.internal(name));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / nbImg, spriteSheet.getHeight());
        int index = 0;
        for (int i = 0; i < nbImg; i++) {
            texReg[index++] = tmp[0][i];
        }
        stateTime = 0f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();

        if (running && !jumping) {
            currentAnimation = walkAnimation;
        } else if (jumping) {
            currentAnimation = jumpAnimation;
        } else {
            currentAnimation = idleAnimation;
        }

        if(runnerDir==direction.LEFT)
        {
//            spriteWidth=-Constants.RUNNER_WIDTH*Constants.SCALE;
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH/2  + Constants.RUNNER_WIDTH* Constants.SCALE, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                    (-Constants.RUNNER_WIDTH*Constants.SCALE)*2, Constants.RUNNER_HEIGHT * Constants.SCALE);

        }else
        {
//            spriteWidth=Constants.RUNNER_WIDTH*Constants.SCALE;
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH/2  - Constants.RUNNER_WIDTH* Constants.SCALE, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                    (Constants.RUNNER_WIDTH*Constants.SCALE)*2, Constants.RUNNER_HEIGHT * Constants.SCALE);

        }

//        batch.draw(currentAnimation.getKeyFrame(stateTime, true), (Constants.APP_WIDTH  - Constants.RUNNER_WIDTH* Constants.SCALE)/2, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
//                Constants.RUNNER_WIDTH*Constants.SCALE, Constants.RUNNER_HEIGHT * Constants.SCALE);

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
        if (!dodging) {
            running = true;
            getUserData().setLinearVelocity(new Vector2(10f, body.getLinearVelocity().y));
//            body.setLinearVelocity(10f,0);
            runnerDir = direction.RIGHT;
        }
    }

    public void runLeft() {
        if (!dodging) {
            getUserData().setLinearVelocity(new Vector2(-10f, body.getLinearVelocity().y));
//            body.setLinearVelocity(-10f,0);
            running = true;
            runnerDir = direction.LEFT;
        }
    }

    public void stopRunning() {
        running = false;
        if (!dodging)
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        //set dataSpeed to 0 when runner stop running
        getUserData().setLinearVelocity(body.getLinearVelocity());
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

    public boolean isJumping() {
        return jumping;
    }
}
