package actors;

import box2d.RunnerUserData;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class Runner extends GameActor {

    private boolean jumping;
    private boolean dodging;
    private boolean running;


    public Runner(Body body) {
        super(body);
    }

    @Override
    public RunnerUserData getUserData() {
        return (RunnerUserData) userData;
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);
        getUserData().setRunningPosition(body.getPosition());
        if(running)
            body.setLinearVelocity(getUserData().getLinearVelocity().x,body.getLinearVelocity().y);
    }
    public void runRight(){
        if(!(dodging)){
            running=true;
            getUserData().setLinearVelocity(new Vector2(10f,body.getLinearVelocity().y));
//            body.setLinearVelocity(10f,0);
        }
    }
    public void runLeft(){
        if(!(dodging)){
            getUserData().setLinearVelocity(new Vector2(-10f,body.getLinearVelocity().y));
//            body.setLinearVelocity(-10f,0);
            running=true;
        }
    }
    public void stopRunning(){
        running=false;
        if(!dodging)
           body.setLinearVelocity(0,body.getLinearVelocity().y);
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
        body.setTransform(body.getWorldCenter(),0f);
    }

    public boolean isDodging() {
        return dodging;
    }
}
