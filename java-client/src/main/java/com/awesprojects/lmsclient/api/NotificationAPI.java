package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.Notification;
import com.awesprojects.lmsclient.utils.requests.LongPollRequest;

import java.util.HashMap;
import java.util.logging.Logger;

public class NotificationAPI {


    public static final String NAME = "NotificationAPI";
    private static final Logger log = Logger.getLogger(NAME);

    static {
        log.fine("notification api log configured");
    }

    private static HashMap<Integer,NotificationConnectionReference> connections;

    public static NotificationConnectionReference create(LongPollRequest request){
        NotificationConnectionReference connectionReference = new NotificationConnectionReference(request.hashCode(),request);
        connections.put(request.hashCode(),connectionReference);
        return connectionReference;
    }

    private static void remove(int id){
        connections.remove(id);
    }

    public interface NotificationReceiver{
        int onReceiveNotification(Notification notification);
    }

    private static class NotificationConnectionReference {

        private int id;

        private LongPollRequest request;

        public NotificationConnectionReference(int id, LongPollRequest request){
            this.id = id;
            this.request = request;
        }

        public int getId(){
            return id;
        }

        public void open(){
            request.open();
        }

        public void close(){
            request.close();
            remove(getId());
        }
    }

}
