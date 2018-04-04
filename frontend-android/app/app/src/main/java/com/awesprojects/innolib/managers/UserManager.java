package com.awesprojects.innolib.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.StartActivity;
import com.awesprojects.innolib.activities.WelcomeActivity;
import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.Responsable;

import java.util.logging.Logger;

import static android.content.Context.MODE_PRIVATE;
import static com.awesprojects.innolib.activities.StartActivity.PREFERENCE_IS_SIGNED_IN;

/**
 * Created by ilya on 2/25/18.
 */

public class UserManager {

    public static final String TAG = "UserManager";
    private static final Logger log = Logger.getLogger(TAG);

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
        void onResponse(Responsable responsable);
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
        log.finest("requested user type string for type = "+type);
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

    public boolean signOut(Context context){
        SharedPreferences preferences = context.getSharedPreferences(InnolibApplication.PREFERENCES_APPLICATION_STATE, MODE_PRIVATE);
        //SharedPreferences signInPreferences = context.getSharedPreferences(InnolibApplication.PREFERENCES_SIGNIN_METHODS, MODE_PRIVATE);
        preferences.edit()
                .putBoolean(PREFERENCE_IS_SIGNED_IN, false).apply();
        SecureStorageManager.getInstance().beginTransaction()
                .put("USER_ID",null)
                .put("USER_PASSWORD",null).commit();
        return true;
    }



    public void signInAsync(Context context,int userId,String password,final OnSignInCallback callback){
        SignInAsyncTask signInAsyncTask = new SignInAsyncTask();
        signInAsyncTask.setCallback(callback);
        signInAsyncTask.execute(userId,password);
    }

    public interface OnSignInCallback{
        void onSignIn(Responsable responsable);
    }

    private static class SignInAsyncTask extends AsyncTask<Object, Integer, Responsable>{

        OnSignInCallback mCallback;

        public void setCallback(OnSignInCallback callback){
            mCallback = callback;
        }

        @Override
        protected Responsable doInBackground(Object... objects) {
            return UsersAPI.getAccessToken((int)objects[0],(String)objects[1]);
        }

        @Override
        protected void onPostExecute(Responsable responsable) {
            super.onPostExecute(responsable);
            if (mCallback!=null)
                mCallback.onSignIn(responsable);
        }
    }

}
