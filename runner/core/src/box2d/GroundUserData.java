package box2d;

import enums.UserDataType;

/**
 * Created by Lancelot on 23.08.2016.
 */
public class GroundUserData extends UserData {
    public GroundUserData(){
        super();
        userDataType= UserDataType.GROUND;
    }
}
