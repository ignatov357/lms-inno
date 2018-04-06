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

    public NotificationAPI.NotificationReader create() {
        return new DefaultNotificationReader(createReference());
    }

    private NotificationConnectionReference createReference() {
        LongPollRequest.Builder builder = RequestFactory.longPoll();
        builder.withURL("/notifications");
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

        @ApiCall(path = "/users/getNotifications")
        public void run(AccessToken accessToken) {
            while (!isWaitingToDie) {
                GetRequest.Builder gr = RequestFactory.get();
                gr.withURL("/users/getNotifications");
                gr.withHeader("Access-Token", accessToken.getToken());
                String str = gr.create().execute();
                if (Response.getResultCode(str) == Response.STATUS_OK) {
                    //log.info("notifications received : " + str);
                    try {
                        JSONArray notifyArray = Response.getJsonArrayBody(str);
                        ServerNotification[] notifications = new ServerNotification[notifyArray.length()];
                        for (int i = 0; i < notifications.length; i++) {
                            notifications[i] = ServerNotification.parse(notifyArray.getJSONObject(i));
                            if (notificationReceiver != null)
                                notificationReceiver.onReceiveNotification(notifications[i]);
                        }
                    } catch (Throwable t) {
                        log.warning("error message parce : " + t.toString());
                    }
                } else {
                    log.warning("unable to parse notifications");
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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

        public NotificationConnectionReference(NotificationAPIImpl notificationAPI, int id, LongPollRequest request) {
            this.notificationAPI = notificationAPI;
            this.id = id;
            this.request = request;
        }

        public int getId() {
            return id;
        }

        public NotificationConnectionReference open() {
            request.open();
            try {
                in = request.getSocket().getInputStream();
                out = request.getSocket().getOutputStream();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
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
