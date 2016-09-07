package actors;


import box2d.EndLevelUserData;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by damien.gygi on 05.09.2016.
 */
public class EndLevel extends GameActor {

    public EndLevel(Body body) {
        super(body);
    }

    /**
     * @return a endLevel typed userData
     */
    @Override
    public EndLevelUserData getUserData() {
        return (EndLevelUserData)userData;
    }
}
