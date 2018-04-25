package com.awesprojects.innolib;

import android.app.Application;
import android.content.Intent;
import android.os.Process;

import com.awesprojects.innolib.activities.LogActivity;
import com.awesprojects.innolib.managers.SecureStorageManager;
import com.awesprojects.innolib.utils.logger.LogSystem;
import com.awesprojects.lmsclient.api.data.AccessToken;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ilya on 2/23/18.
 */

public class InnolibApplication extends Application{

    public static final String TAG = "Application";
    public static Logger log = Logger.getLogger(TAG);

    public static final String PREFERENCES_APPLICATION_STATE = "application_state";
    public static final String PREFERENCES_SIGNIN_METHODS = "sign_in";

    private static AccessToken mAccessToken;
    private static InnolibApplication mInstance;

    public static InnolibApplication getInstance(){
        return mInstance;
    }

    public static AccessToken loadCachedToken(){
        String token = SecureStorageManager.getInstance().get("CACHED_ACCESS_TOKEN");
        AccessToken accessToken = new AccessToken(token,-1);
        InnolibApplication.setAccessToken(accessToken);
        return accessToken;
    }

    public static AccessToken getAccessToken(){
        return mAccessToken;
    }

    public static void setAccessToken(AccessToken accessToken){
        mAccessToken = accessToken;
        SecureStorageManager.getInstance().beginTransaction().put("CACHED_ACCESS_TOKEN",accessToken.getToken()).commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        LogSystem.ensureInit();
        log.info("application started");
        getMainLooper().getThread().setUncaughtExceptionHandler(this::onFatalException);
    }

    public void onFatalException(Thread thread,Throwable throwable){
        log.severe("uncaught exception in app thread : "+throwable.toString());
        log.log(Level.SEVERE,"full exception info:",throwable);
        log.severe("application is forced to close");
        Intent logActivityIntent = new Intent(this, LogActivity.class);
        startActivity(logActivityIntent);
        Process.killProcess(Process.myPid());
        System.exit(10);
    }


}
