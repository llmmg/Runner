package box2d;

import com.badlogic.gdx.math.Vector2;
import utils.Constants;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class RunnerUserData extends UserData {

    private Vector2 jumpingLinearImpulse;
    private Vector2 runningPosition = new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y);
    private Vector2 dodgePosition = new Vector2(Constants.RUNNER_DODGE_X, Constants.RUNNER_DODGE_Y);
    private Vector2 linearVelocity;

    public RunnerUserData() {
        super();
        jumpingLinearImpulse = Constants.RUNNER_JUMPING_LINEAR_IMPULSE;
        userDataType = userDataType.RUNNER;
        linearVelocity=Constants.RUNNER_VELOCITY;
    }

    public void setLinearVelocity(Vector2 linearVelocity)
    {
        this.linearVelocity=linearVelocity;
    }
    public Vector2 getLinearVelocity()
    {
        return linearVelocity;
    }

    public Vector2 getJumpingLinearImpulse() {
        return jumpingLinearImpulse;
    }

    public void setJumpingLinearImpulse(Vector2 jumpingLinearImpulse) {
        this.jumpingLinearImpulse = jumpingLinearImpulse;
    }
    public float getDodgeAngle(){
        //radians
        return (float) (-90f*(Math.PI/180f));
    }
    public Vector2 getRunningPosition() {
        return runningPosition;
    }
    public void setRunningPosition(Vector2 runningPosition)
    {
        this.runningPosition=runningPosition;
    }
//    public Vector2 getDodgePosition() {
//        return dodgePosition;
//    }
//    public void setDodgePosition(Vector2 dodgePosition)
//    {
//        this.dodgePosition=dodgePosition;
//    }
}
