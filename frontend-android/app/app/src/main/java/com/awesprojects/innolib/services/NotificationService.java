package com.awesprojects.innolib.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.activities.StartActivity;
import com.awesprojects.innolib.managers.NotificationManager;
import com.awesprojects.lmsclient.api.NotificationAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.ServerNotification;

import java.util.logging.Logger;

public class NotificationService extends Service {

    public static final String TAG = "NotifService";
    public static Logger log = Logger.getLogger(TAG);
    private NotificationHandler mNotificationHandler;
    NotificationAPI.NotificationReader mReader;
    AccessToken mAccessToken;

    public NotificationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ensureToken();
        mNotificationHandler = new NotificationHandler(this);
        mReader = NotificationAPI.create();
        new NotificationReceiverThread(this,mAccessToken,mReader).start();
    }

    public void ensureToken(){
        SharedPreferences sp = getSharedPreferences(InnolibApplication.PREFERENCES_APPLICATION_STATE, MODE_PRIVATE);
        boolean signedIn = sp.getBoolean(StartActivity.PREFERENCE_IS_SIGNED_IN,false);
        if (!signedIn)
            stopSelf();
        mAccessToken = InnolibApplication.getAccessToken();
        if (mAccessToken==null)
            mAccessToken = InnolibApplication.loadCachedToken();
        if (mAccessToken==null) {
            stopSelf();
            log.warning("you are signed in, but access token was not found");
        }
    }

    @Override
    public void onDestroy() {
        mNotificationHandler = null;
        super.onDestroy();
    }

    public void onNotificationReceived(ServerNotification serverNotification){
        NotificationManager.Notification notification = new NotificationManager.Notification();
        notification.withId(serverNotification.getId());
        notification.withTitle("Notification from server");
        notification.withSubtitle(serverNotification.getDescription());
        NotificationManager.getInstance().modifyNotification(this,notification);
    }

    private static class NotificationHandler extends Handler{

        private final NotificationService mService;

        public NotificationHandler(NotificationService service){
            mService = service;
        }

        @Override
        public void handleMessage(Message msg) {
            mService.onNotificationReceived((ServerNotification)msg.obj);
        }
    }

    public static class NotificationReceiverThread extends Thread{

        final AccessToken mAccessToken;
        final NotificationService mService;
        final NotificationAPI.NotificationReader mReader;

        public NotificationReceiverThread(NotificationService service, AccessToken accessToken, NotificationAPI.NotificationReader reader){
            mService = service;
            mAccessToken = accessToken;
            mReader = reader;
        }

        public void run(){
            mReader.setNotificationReceiver(this::onReceiveNotification);
            mReader.run(mAccessToken);
        }

        public int onReceiveNotification(ServerNotification notification){
            Message msg = new Message();
            msg.obj = notification;
            mService.mNotificationHandler.sendMessage(msg);
            return 1;
        }
    }

}
