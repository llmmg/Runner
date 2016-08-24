package box2d;

import enums.UserDataType;

/**
 * Created by Lancelot on 23.08.2016.
 */
public abstract class UserData {

    protected UserDataType userDataType;

    public UserData() {
    }

    public UserDataType getUserDataType() {
        return userDataType;
    }
}
