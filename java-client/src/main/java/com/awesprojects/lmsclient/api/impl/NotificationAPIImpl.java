package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.NotificationAPI;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.ServerNotification;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.LongPollRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Logger;

public class NotificationAPIImpl implements INotificationAPI {

    public NotificationAPIImpl() {
        log.fine("notification api log configured");
        connections = new HashMap<>();
    }

    public static final String NAME = "NotificationAPI";
    private static final Logger log = Logger.getLogger(NAME);

    private HashMap<Integer, NotificationConnectionReference> connections;

    public NotificationAPI.NotificationReader create(AccessToken token) {
        return new DefaultNotificationReader(createReference(token));
    }

    private NotificationConnectionReference createReference(AccessToken accessToken) {
        LongPollRequest.Builder builder = RequestFactory.longPoll();
        builder.withURL("/"+accessToken.getToken());
        builder.onPort(3000);
        LongPollRequest request = builder.create();
        NotificationConnectionReference connectionReference = new NotificationConnectionReference(this, request.hashCode(), request);
        connections.put(request.hashCode(), connectionReference);
        return connectionReference;
    }

    private void remove(int id) {
        connections.remove(id);
    }

    private static class DefaultNotificationReader extends NotificationAPI.NotificationReader {

        final NotificationConnectionReference notificationConnectionReference;
        private NotificationAPI.NotificationReceiver notificationReceiver;
        boolean isWaitingToDie = false;

        public DefaultNotificationReader(final NotificationConnectionReference reference) {
            notificationConnectionReference = reference;
        }


        public void setNotificationReceiver(NotificationAPI.NotificationReceiver receiver) {
            notificationReceiver = receiver;
        }

        @ApiCall(path = "/${AccessToken}")
        public void run() {
            notificationConnectionReference.open();
            if (!notificationConnectionReference.isOpened()) return;
            try{
                byte[] buffer = new byte[4096];
                while (true){
                    int read = notificationConnectionReference.in.read();
                    System.out.print(read+" ");
                    //notificationConnectionReference.read(buffer,0,4096);
                    //String response = new String(buffer).trim();
                    //System.out.println(response);
                }
            }catch(Throwable t){}
        }

        public void close() {
            isWaitingToDie = true;
            notificationConnectionReference.close();
        }

    }

    private static class NotificationConnectionReference {

        private int id;

        private LongPollRequest request;
        private InputStream in;
        private OutputStream out;
        private NotificationAPIImpl notificationAPI;
        private boolean opened;

        public NotificationConnectionReference(NotificationAPIImpl notificationAPI, int id, LongPollRequest request) {
            this.notificationAPI = notificationAPI;
            this.id = id;
            this.request = request;
        }

        public int getId() {
            return id;
        }

        public boolean isOpened(){
            return opened;
        }

        public NotificationConnectionReference open() {
            try {
                request.open();
                in = request.getSocket().getInputStream();
                out = request.getSocket().getOutputStream();
                opened = true;
                log.finest("notification reference created succeed");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                opened = false;
                log.warning("unable to open notification reference");
            }
            return this;
        }

        public int read(byte[] bytes, int offset, int length) {
            try {
                return in.read(bytes, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }

        public void write(byte[] bytes, int offset, int length) {
            try {
                out.write(bytes, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void close() {
            request.close();
            notificationAPI.remove(getId());
        }
    }

}
