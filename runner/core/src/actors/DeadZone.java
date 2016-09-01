package actors;

import box2d.DeadZoneUserData;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by damien.gygi on 01.09.2016.
 */
public class DeadZone extends GameActor {

    public DeadZone(Body body) {
        super(body);
    }

    @Override
    public DeadZoneUserData getUserData() {
        return (DeadZoneUserData)userData;
    }
}
