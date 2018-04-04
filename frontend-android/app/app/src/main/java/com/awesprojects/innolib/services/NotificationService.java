package com.awesprojects.innolib.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.awesprojects.innolib.managers.NotificationManager;
import com.awesprojects.lmsclient.api.NotificationAPI;
import com.awesprojects.lmsclient.api.data.ServerNotification;

public class NotificationService extends Service {

    private NotificationAPI.NotificationReader mReader;
    private AsyncNotificationReader mReaderAsyncTask;

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
        mReaderAsyncTask = new AsyncNotificationReader(this);
        mReaderAsyncTask.execute(mReader);
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

    public static class AsyncNotificationReader extends AsyncTask<NotificationAPI.NotificationReader,ServerNotification,Void>{

        private final NotificationService mService;
        NotificationAPI.NotificationReader mReader;

        public AsyncNotificationReader(NotificationService service){
            super();
            mService = service;
        }

        @Override
        protected Void doInBackground(NotificationAPI.NotificationReader... notificationReaders) {
            mReader = notificationReaders[0];
            mReader.setNotificationReceiver(this::onNotificationRead);
            mReader.run();
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