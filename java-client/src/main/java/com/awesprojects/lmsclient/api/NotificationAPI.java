package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.ServerNotification;
import com.awesprojects.lmsclient.api.impl.APIFactory;
import com.awesprojects.lmsclient.api.impl.INotificationAPI;
import com.awesprojects.lmsclient.api.impl.NotificationAPIImpl;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.api.internal.Method;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.api.internal.ResponseType;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.LongPollRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class NotificationAPI {

    private static INotificationAPI impl;

    static {
        impl = APIFactory.getDefault().getNotificationAPI();

    }

    @ApiCall(
            path = "/${AccessToken}",
            method = Method.SOCKET,
            port = 3000
    )
    @ResponseType(
            returns = NotificationReader.class
    )
    public static NotificationAPI.NotificationReader create(AccessToken token){
        return impl.create(token);
    }




    public static final int NOTIFICATION_READ = 1;
    public static final int NOTIFICATION_UNREAD = 0;

    public interface NotificationReceiver{
        int onReceiveNotification(ServerNotification notification);
    }

    public interface NotificationChangeListener{
        void onConnected();
        void onDisconnected();
    }

    public static abstract class NotificationReader implements Responsable {
        public abstract void setNotificationReceiver(NotificationReceiver receiver);
        public abstract void setNotificationChangeListener(NotificationChangeListener listener);
        public abstract void run();
        public abstract void close();
    }


}
