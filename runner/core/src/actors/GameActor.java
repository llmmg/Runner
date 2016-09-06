package actors;

import box2d.UserData;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Lancelot on 23.08.2016.
 */
public abstract class GameActor extends Actor {

    protected Body body;
    protected UserData userData;

    /**
     * @param body A rigid body
     */
    public GameActor(Body body) {
        this.body = body;
        this.userData= (UserData)body.getUserData();
    }

    public abstract UserData getUserData();
}
