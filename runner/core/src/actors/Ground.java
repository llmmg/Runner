package actors;

import box2d.GroundUserData;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class Ground extends GameActor {

    public Ground(Body body) {
        super(body);
    }

    /**
     * @return a ground typed userData
     */
    @Override
    public GroundUserData getUserData() {
        return (GroundUserData)userData;
    }
}
