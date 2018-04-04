package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.ServerNotification;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.utils.requests.LongPollRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class NotificationAPI {


    public static final String NAME = "NotificationAPI";
    private static final Logger log = Logger.getLogger(NAME);

    static {
        log.fine("notification api log configured");
    }

    private static HashMap<Integer,NotificationConnectionReference> connections;

    public static NotificationReader create(){
        return new NotificationReader(createReference());
    }

    private static NotificationConnectionReference createReference(){
        LongPollRequest.Builder builder = RequestFactory.longPoll();
        builder.withURL("/notifications");
        LongPollRequest request = builder.create();
        NotificationConnectionReference connectionReference = new NotificationConnectionReference(request.hashCode(),request);
        connections.put(request.hashCode(),connectionReference);
        return connectionReference;
    }

    private static void remove(int id){
        connections.remove(id);
    }



    public static final int NOTIFICATION_READ = 1;
    public static final int NOTIFICATION_UNREAD = 0;

    public interface NotificationReceiver{
        int onReceiveNotification(ServerNotification notification);
    }

    private static class NotificationConnectionReference {

        private int id;

        private LongPollRequest request;
        private InputStream in;
        private OutputStream out;

        public NotificationConnectionReference(int id, LongPollRequest request){
            this.id = id;
            this.request = request;
        }

        public int getId(){
            return id;
        }

        public NotificationConnectionReference open(){
            request.open();
            try {
                in = request.getSocket().getInputStream();
                out = request.getSocket().getOutputStream();
            }catch(Throwable throwable){
                throwable.printStackTrace();
            }
            return this;
        }

        public int read(byte[] bytes,int offset,int length){
            try {
                return in.read(bytes, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public void write(byte[] bytes,int offset,int length){
            try {
                out.write(bytes, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public void close(){
            request.close();
            remove(getId());
        }
    }

    public static class NotificationReader {

        final NotificationConnectionReference notificationConnectionReference;
        private NotificationReceiver notificationReceiver;

        public NotificationReader(final NotificationConnectionReference reference){
            notificationConnectionReference = reference;
        }

        public void setNotificationReceiver(NotificationReceiver receiver){
            notificationReceiver = receiver;
        }

        @ApiCall(path = "/notifications")
        public void run(){

        }

        public void close(){
            notificationConnectionReference.close();
        }

    }

}
