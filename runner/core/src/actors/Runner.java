package actors;

import box2d.RunnerUserData;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    private boolean falling;
    private boolean rising; //used to know when the contact(collision) has to be ignored for landing

    public enum direction {
        LEFT,
        RIGHT
    }

    private Animation currentAnimation;

    //cat sprite
    private Animation runningAnimation;
    private Animation idleAnimationCat;
    private Animation slideAnimation;
    private Animation jumpAnimation;
    private Animation fallAnimation;
    private TextureRegion[] runningFrames;
    private TextureRegion[] idleFramesCat;
    private TextureRegion[] slideFrames;
    private TextureRegion[] jumpFrames;
    private TextureRegion[] fallFrames;

    private Texture spriteSheet;

    float stateTime;

    //runner direction
    private direction runnerDir;

    /**
     * @param body A rigid body
     */
    public Runner(Body body) {
        super(body);

        TextureAtlas textureAtlas = new TextureAtlas(Constants.CAT_ATLAS_PATH);
        runningAnimation = initAnimation(textureAtlas, runningFrames, Constants.CAT_RUN_REGION_NAMES, 0.08f);
        idleAnimationCat = initAnimation(textureAtlas, idleFramesCat, Constants.CAT_IDLE_REGION_NAMES, 0.1f);
        jumpAnimation = initAnimation(textureAtlas, jumpFrames, Constants.CAT_JUMP_REGION_NAMES, 0.1f);
        fallAnimation = initAnimation(textureAtlas, fallFrames, Constants.CAT_FALL_REGION_NAMES, 0.1f);
        slideAnimation = initAnimation(textureAtlas, slideFrames, Constants.CAT_SLIDE_REGION_NAMES, 0.1f);
    }

    /**
     * Create and return sprite animation from picture and data sheet
     *
     * @param textureAtlas   Object that contain file path to the sprites data sheet
     * @param frames         Rectangular area of a texture. it's initialised in the function
     * @param regionsNames   Name of sprites from data sheet
     * @param animationSpeed Time between frames
     * @return Animation
     */
    private Animation initAnimation(TextureAtlas textureAtlas, TextureRegion[] frames, String[] regionsNames, float animationSpeed) {
        frames = new TextureRegion[regionsNames.length];
        for (int i = 0; i < regionsNames.length; i++) {
            String path = regionsNames[i];
            frames[i] = textureAtlas.findRegion(path);
        }
        return new Animation(animationSpeed, frames);
    }

    /**
     * @param texReg
     * @param name
     * @param nbImg
     * @deprecated replaced by initAnimation
     */
    public void createAnimation(TextureRegion[] texReg, String name, int nbImg) {
        spriteSheet = new Texture(Gdx.files.internal(name));
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / nbImg, spriteSheet.getHeight());
        int index = 0;
        for (int i = 0; i < nbImg; i++) {
            texReg[index++] = tmp[0][i];
        }
        stateTime = 0f;
    }

    /**
     * See libgdx draw doc here:<a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Actor.html#draw-com.badlogic.gdx.graphics.g2d.Batch-float-">draw</a>
     * define which animation play according to actual runner state
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        stateTime += Gdx.graphics.getDeltaTime();

        if (running && !jumping) {
            currentAnimation = runningAnimation;
        } else if (falling) {
            currentAnimation = fallAnimation;
            rising = false;
        } else if (jumping) {
            currentAnimation = jumpAnimation;
            rising = true;
        } else if (dodging) {
            currentAnimation = slideAnimation;
        } else {
            currentAnimation = idleAnimationCat;
        }

        //Draw animation to the left or right (depend on runner direction)
        if (runnerDir == direction.LEFT) {
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH / 2 + Constants.RUNNER_WIDTH * Constants.SCALE / 2, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                    (-Constants.RUNNER_WIDTH * Constants.SCALE), Constants.RUNNER_HEIGHT * Constants.SCALE);
        } else {
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH / 2 - Constants.RUNNER_WIDTH * Constants.SCALE / 2, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                    (Constants.RUNNER_WIDTH * Constants.SCALE), Constants.RUNNER_HEIGHT * Constants.SCALE);
        }
    }

    /**
     * Getter to UserData where runner information like speed or positions are stored
     *
     * @return
     */
    @Override
    public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }

    /**
     * Libgdx act doc : <a href="http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Actor.html#act-float-">act</a>
     * <p>
     * Update runner position and speed with data stored in UserData object.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        getUserData().setRunningPosition(body.getPosition());

        if (running)
            body.setLinearVelocity(getUserData().getLinearVelocity().x, body.getLinearVelocity().y);

        if (dodging)
            getUserData().setLinearVelocity(body.getLinearVelocity());

        //if runner is falling, it's speed is negative
        falling = body.getLinearVelocity().y < 0;
    }

    /**
     * Make runner go right.
     * A linear velocity of (10f, actual body y velocity) is applied to the runner UserData
     */
    public void runRight() {
        if (!dodging) {
            running = true;
            getUserData().setLinearVelocity(new Vector2(10f, body.getLinearVelocity().y));
            runnerDir = direction.RIGHT;
        }
    }

    /**
     * Make runner go left.
     * A linear velocity of (-10f, actual body y velocity) is applied to the runner UserData
     */
    public void runLeft() {
        if (!dodging) {
            getUserData().setLinearVelocity(new Vector2(-10f, body.getLinearVelocity().y));
            running = true;
            runnerDir = direction.LEFT;
        }
    }

    /**
     * Stop the runner
     */
    public void stopRunning() {
        running = false;
        if (!dodging) {
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
        //set dataSpeed to 0 when runner stop running
        getUserData().setLinearVelocity(body.getLinearVelocity());
    }

    /**
     * Make the runner jump
     */
    public void jump() {
        if (!(jumping || dodging) && !falling) {
//            System.out.println("jump");
            body.applyLinearImpulse(getUserData().getJumpingLinearImpulse(), body.getWorldCenter(), true);
            jumping = true;
        }
    }

    /**
     * Called when the runner touch the ground after a jump
     */
    public void landed() {
        jumping = false;
    }

    /**
     * Make the runner crouch and slide if he's running
     */
    public void dodge() {
        if (!jumping) {
//            System.out.println("dodge");
            dodging = true;
            getUserData().setLinearVelocity(new Vector2(0f, body.getLinearVelocity().y));
        }
    }

    /**
     * Interupt crounch or slide
     */
    public void stopDodge() {
        dodging = false;
        stopRunning();
//        System.out.println("stopDodge");
    }

    /**
     * Getter for dodging boolean
     *
     * @return true if runner is dodging
     */
    public boolean isDodging() {
        return dodging;
    }

    /**
     * Getter for jumping bolean
     *
     * @return true if runner is jumping
     */
    public boolean isJumping() {
        return jumping;
    }

    /**
     * @return true if runner is ascending
     */
    public boolean isRising() {
        return rising;
    }

    /**
     * @return direction of the runner (LEFT or RIGHT)
     */
    public direction getRunnerDir() {
        return runnerDir;
    }
}
