package com.awesprojects.innolib.managers;

import android.app.Notification;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.app.NotificationCompat;

import com.awesprojects.innolib.R;

import java.util.logging.Logger;

/**
 * Created by ilya on 4/1/18.
 */

public class NotificationManager {

    public static final String TAG = "NotificationManager";
    private static final Logger log = Logger.getLogger(TAG);

    public static class Notification{
        private String mTitle;
        private String mSubtitle;
        @DrawableRes
        private int mIcon;
        private int mId;

        public Notification withTitle(String title){
            mTitle = title;
            return this;
        }

        public Notification withSubtitle(String subtitle){
            mSubtitle = subtitle;
            return this;
        }

        public Notification withIcon(@DrawableRes int icon){
            mIcon = icon;
            return this;
        }

        public Notification withId(int id){
            mId = id;
            return this;
        }

        public int getId(){
            return mId;
        }

        public int getIcon() {
            return mIcon;
        }

        public String getSubtitle() {
            return mSubtitle;
        }

        public String getTitle() {
            return mTitle;
        }

    }

    private static NotificationManager mInstance;

    public static NotificationManager getInstance() {
        if (mInstance==null)
            mInstance = new NotificationManager();
        return mInstance;
    }

    public void addNotification(Context context,Notification notification){
        if (notification.getIcon()==0)
            notification.withIcon(R.drawable.ic_library_books_black_24dp);
        android.app.NotificationManager nm = (android.app.NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(notification.getIcon())
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getSubtitle())
                .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle()
                .bigText(notification.getSubtitle())
                .setBigContentTitle(notification.getTitle()));
        android.app.Notification n = builder.build();
        nm.notify(notification.getId(),n);
    }

    public void modifyNotification(Context context,Notification notification){
        addNotification(context,notification);
    }
}
