package com.awesprojects.innolib.managers;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.Responsable;

/**
 * Created by ilya on 2/25/18.
 */

public class UserManager {

    private static UserManager mInstance;

    public static UserManager getInstance() {
        if (mInstance==null)
            mInstance = new UserManager();
        return mInstance;
    }

    protected UserManager(){

    }

    public static int getUserType(User user){
        return user.getType();
    }

    public interface OnResponseCallback{
        public void onResponse(Responsable responsable);
    }

    public void getUserAsync(AccessToken accessToken,int userID,OnResponseCallback callback){
        new Thread(() -> {
            Responsable r = ManageAPI.Users.getUser(accessToken, userID);
            if (callback != null)
                callback.onResponse(r);
        }).start();
    }

    public void getUserInfoAsync(AccessToken accessToken,OnResponseCallback callback){
        new Thread(() -> {
            Responsable r = UsersAPI.getUserInfo(accessToken);
            if (callback != null)
                callback.onResponse(r);
        }).start();
    }

}
