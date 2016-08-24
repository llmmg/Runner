package actors;

import box2d.GroundUserData;
import box2d.UserData;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class Ground extends GameActor {

    public Ground(Body body) {
        super(body);
    }

    @Override
    public GroundUserData getUserData() {
        return (GroundUserData)userData;
    }
}
