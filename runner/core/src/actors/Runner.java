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

    public enum direction {
        LEFT,
        RIGHT
    }

//    private Animation walkAnimation;
//    private Animation idleAnimation;
//    private Animation jumpAnimation;
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
//    private TextureRegion[] walkFrames;
//    private TextureRegion[] idleFrames;
//    private TextureRegion[] jumpFrames;

    private int imgWalk = 12;
    private int imgIdle = 3;
    private int imgJump = 3;
    private float spriteWidth;

    float stateTime;

    //runner direction
    private direction runnerDir;

    public Runner(Body body) {
        super(body);

        TextureAtlas textureAtlas = new TextureAtlas(Constants.CAT_ATLAS_PATH);
        runningAnimation= initAnimation(textureAtlas,runningFrames,Constants.CAT_RUN_REGION_NAMES,0.08f);
        idleAnimationCat= initAnimation(textureAtlas,idleFramesCat,Constants.CAT_IDLE_REGION_NAMES,0.1f);
        jumpAnimation= initAnimation(textureAtlas,jumpFrames,Constants.CAT_JUMP_REGION_NAMES,0.1f);
        fallAnimation= initAnimation(textureAtlas,fallFrames,Constants.CAT_FALL_REGION_NAMES,0.1f);
        slideAnimation= initAnimation(textureAtlas,slideFrames,Constants.CAT_SLIDE_REGION_NAMES,0.1f);


//        walkFrames = new TextureRegion[imgWalk];
//        createAnimation(walkFrames, Constants.CHARACTER_RUN_PATH, imgWalk);
//        walkAnimation = new Animation(0.05f, walkFrames);

//        idleFrames = new TextureRegion[imgIdle];
//        createAnimation(idleFrames, Constants.CHARACTER_IDLE_PATH, imgIdle);
//        idleAnimation = new Animation(0.1f, idleFrames);

//        jumpFrames = new TextureRegion[imgJump];
//        createAnimation(jumpFrames, Constants.CHARACTER_JUMP_PATH, imgJump);
//        jumpAnimation = new Animation(0.1f, jumpFrames);

    }

    //create sprite animation from picture and datasheet
    private Animation initAnimation(TextureAtlas textureAtlas, TextureRegion[] frames, String[] regionsNames, float animationSpeed) {
        frames = new TextureRegion[regionsNames.length];
        for (int i = 0; i < regionsNames.length; i++) {
            String path = regionsNames[i];
            frames[i] = textureAtlas.findRegion(path);
        }

        return new Animation(animationSpeed, frames);
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
            currentAnimation = runningAnimation;
        }else if(falling) {
            System.out.println("fall anim");
            currentAnimation = fallAnimation;
        }else if (jumping) {
            System.out.println("jump anim");
            currentAnimation = jumpAnimation;
        }else if (dodging)
        {
            currentAnimation=slideAnimation;
        }
        else
        {
            currentAnimation=idleAnimationCat;
        }

        if (runnerDir == direction.LEFT) {
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH / 2 + Constants.RUNNER_WIDTH * Constants.SCALE / 2, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                    (-Constants.RUNNER_WIDTH * Constants.SCALE), Constants.RUNNER_HEIGHT * Constants.SCALE);

        } else {
            batch.draw(currentAnimation.getKeyFrame(stateTime, true), Constants.APP_WIDTH / 2 - Constants.RUNNER_WIDTH * Constants.SCALE / 2, (Constants.APP_HEIGHT - Constants.RUNNER_HEIGHT * Constants.SCALE) / 2,
                    (Constants.RUNNER_WIDTH * Constants.SCALE), Constants.RUNNER_HEIGHT * Constants.SCALE);

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
        if(dodging)
            getUserData().setLinearVelocity(body.getLinearVelocity());

        falling=body.getLinearVelocity().y<0;
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
        if(!dodging){
            body.setLinearVelocity(0, body.getLinearVelocity().y);
        }
        //set dataSpeed to 0 when runner stop running
        getUserData().setLinearVelocity(body.getLinearVelocity());
        System.out.println("stop running");
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
//            body.setTransform(body.getWorldCenter(), getUserData().getDodgeAngle());
            //-------
            //test
//            if(running)
//            {
//                body.setLinearVelocity(0,0);
//                body.applyLinearImpulse(10f,0,body.getWorldCenter().x,body.getWorldCenter().y,true);
//            }
            //endtest
            //-------
            System.out.println("dodge");
            dodging = true;
            getUserData().setLinearVelocity(new Vector2(0f,body.getLinearVelocity().y));
        }
    }

    public void stopDodge() {
        dodging = false;
        body.setTransform(body.getWorldCenter(), 0f);
        System.out.println("stopDodge");
    }

    public boolean isDodging() {
        return dodging;
    }

    public boolean isJumping() {
        return jumping;
    }
}
