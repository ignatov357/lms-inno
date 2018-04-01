package com.awesprojects.innolib.managers;

import android.content.Context;

import com.awesprojects.innolib.R;
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

    public static String getUserTypeString(Context context, int type){
        switch (type){
            case 0: return context.getResources().getString(R.string.user_type_librarian);
            case 1: return context.getResources().getString(R.string.user_type_student);
            case 2: return context.getResources().getString(R.string.user_type_teacher_assistant);
            case 3: return context.getResources().getString(R.string.user_type_professor);
            case 4: return context.getResources().getString(R.string.user_type_instructor);
            case 5: return context.getResources().getString(R.string.user_type_visiting_professor);
            default: return "Unknown type";
        }
    }

}
