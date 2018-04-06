package com.awesprojects.innolib.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.managers.NotificationManager;
import com.awesprojects.lmsclient.api.NotificationAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.ServerNotification;

public class NotificationService extends Service {

    private NotificationHandler mNotificationHandler;
    NotificationAPI.NotificationReader mReader;

    public NotificationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationHandler = new NotificationHandler(this);
        mReader = NotificationAPI.create();
        new NotificationReceiverThread(this,InnolibApplication.getAccessToken(),mReader).start();
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
