package com.awesprojects.innolib;

import android.app.Application;

import com.awesprojects.lmsclient.api.data.AccessToken;

/**
 * Created by ilya on 2/23/18.
 */

public class InnolibApplication extends Application{

    public static final String PREFERENCES_APPLICATION_STATE = "application_state";
    public static final String PREFERENCES_SIGNIN_METHODS = "sign_in";

    private static AccessToken mAccessToken;
    private static InnolibApplication mInstance;

    public static InnolibApplication getInstance(){
        return mInstance;
    }

    public static AccessToken getAccessToken(){
        return mAccessToken;
    }

    public static void setAccessToken(AccessToken accessToken){
        mAccessToken = accessToken;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }


}
