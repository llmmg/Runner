package utils;

import box2d.UserData;
import com.badlogic.gdx.physics.box2d.Body;
import enums.UserDataType;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class BodyUtils {

    public static boolean bodyIsRunner(Body body) {
        UserData userData = (UserData) body.getUserData();

        return userData != null && userData.getUserDataType() == UserDataType.RUNNER;
    }

    public static boolean bodyIsGround(Body body) {
        UserData userData = (UserData) body.getUserData();
        return userData != null && userData.getUserDataType() == UserDataType.GROUND;
    }
}
