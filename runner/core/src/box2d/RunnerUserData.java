package box2d;

import com.badlogic.gdx.math.Vector2;
import utils.Constants;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class RunnerUserData extends UserData {

    private Vector2 jumpingLinearImpulse;
    private Vector2 runningPosition = new Vector2(Constants.RUNNER_X, Constants.RUNNER_Y);
    private Vector2 linearVelocity;

    public RunnerUserData() {
        super();
        jumpingLinearImpulse = Constants.RUNNER_JUMPING_LINEAR_IMPULSE;
        userDataType = userDataType.RUNNER;
        linearVelocity = Constants.RUNNER_VELOCITY;
    }

    /**
     * Setter for runner velocity
     * @param linearVelocity
     */
    public void setLinearVelocity(Vector2 linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    /**
     * Getter for runner velocity
     * @return linearVelocity
     */
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    /**
     * Getter for runner jumping Impulse
     * @return runner jumping linear impulse
     */
    public Vector2 getJumpingLinearImpulse() {
        return jumpingLinearImpulse;
    }

    /**
     * Getter for actual position of runner
     * @return
     */
    public Vector2 getRunningPosition() {
        return runningPosition;
    }

    /**
     * Setter for runner position
     * @param runningPosition
     */
    public void setRunningPosition(Vector2 runningPosition) {
        this.runningPosition = runningPosition;
    }
}
