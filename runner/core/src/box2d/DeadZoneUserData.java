package box2d;

import enums.UserDataType;

/**
 * Created by damien.gygi on 01.09.2016.
 */
public class DeadZoneUserData extends UserData {
    public DeadZoneUserData(){
        super();
        userDataType= UserDataType.DEADZONE;
    }
}
