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

    private NotificationAPI.NotificationReader mReader;
    private AsyncNotificationReader mReaderAsyncTask;
    private Handler mNotificationHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            onNotificationReceived((ServerNotification)msg.obj);
        }
    };

    public NotificationService() {}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReader = NotificationAPI.create();
        new NotificationReceiverThread(this,InnolibApplication.getAccessToken(),mReader).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onNotificationReceived(ServerNotification serverNotification){
        NotificationManager.Notification notification = new NotificationManager.Notification();
        notification.withId(serverNotification.getId());
        notification.withTitle("Notification from server");
        notification.withSubtitle(serverNotification.getDescription());
        NotificationManager.getInstance().modifyNotification(this,notification);
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
            mReader.setNotificationReceiver((sn) -> {
                Message msg = new Message();
                msg.obj = sn;
                mService.mNotificationHandler.sendMessage(msg);
                return 1;
            });
            mReader.run(mAccessToken);
        }
    }


    public static class AsyncNotificationReader extends AsyncTask<NotificationAPI.NotificationReader,ServerNotification,Void>{

        private final NotificationService mService;
        NotificationAPI.NotificationReader mReader;

        public AsyncNotificationReader(NotificationService service){
            super();
            mService = service;
            ServerNotification sn = new ServerNotification();
            sn.setId(0);
            sn.setDescription("startup");
            onNotificationRead(sn);
        }

        @Override
        protected Void doInBackground(NotificationAPI.NotificationReader... notificationReaders) {

            return null;
        }

        public int onNotificationRead(ServerNotification notification){
            publishProgress(notification);
            return NotificationAPI.NOTIFICATION_READ;
        }

        @Override
        protected void onProgressUpdate(ServerNotification... values) {
            super.onProgressUpdate(values);
            mService.onNotificationReceived(values[0]);
        }
        
    }

}
