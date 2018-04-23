package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.NotificationAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.ServerNotification;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.utils.Config;
import com.awesprojects.lmsclient.utils.sockets.WebSocketClient;
import org.json.JSONObject;

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
        WebSocketClient webSocketClient = null;
        try{
            webSocketClient= new WebSocketClient(Config.getCurrentConfig().getApiDomain()
                                            ,3000,"/"+accessToken.getToken());
        }catch (Throwable t){
            log.severe("web socket client unable to be created : "+t.toString());
            return null;
        }
        NotificationConnectionReference connectionReference = new NotificationConnectionReference(this,
                                                                        webSocketClient.hashCode(),webSocketClient);
        connections.put(webSocketClient.hashCode(), connectionReference);
        return connectionReference;
    }

    private void remove(int id) {
        connections.remove(id);
    }

    private static class DefaultNotificationReader extends NotificationAPI.NotificationReader {

        final NotificationConnectionReference notificationConnectionReference;
        private NotificationAPI.NotificationReceiver notificationReceiver;
        private NotificationAPI.NotificationChangeListener notificationChangeListener;
        boolean isWaitingToDie = false;

        public DefaultNotificationReader(final NotificationConnectionReference reference) {
            notificationConnectionReference = reference;
        }


        public void setNotificationReceiver(NotificationAPI.NotificationReceiver receiver) {
            notificationReceiver = receiver;
        }

        @Override
        public void setNotificationChangeListener(NotificationAPI.NotificationChangeListener listener) {
            notificationChangeListener = listener;
        }

        private void onTextMessage(String msg){
            JSONObject object = new JSONObject(msg);
            if (!object.optString("type","null").equals("notification")) return;
            ServerNotification notification = new ServerNotification();
            notification.setId(msg.hashCode()%100);
            notification.setDescription(object.optString("notificationText"));
            notificationReceiver.onReceiveNotification(notification);
        }

        private void onConnected(){
            if (notificationChangeListener!=null)
                notificationChangeListener.onConnected();
        }

        private void onDisconnected(){
            if (notificationChangeListener!=null)
                notificationChangeListener.onDisconnected();
        }

        @ApiCall(path = "/${AccessToken}")
        public void run() {
            try {
                notificationConnectionReference.webSocketClient.setConnectionStateListener(new WebSocketClient.ConnectionStateListener() {
                    @Override
                    public void onConnected() {
                        DefaultNotificationReader.this.onConnected();
                    }
                    @Override
                    public void onDisconnected() {
                        DefaultNotificationReader.this.onDisconnected();
                    }
                });
                notificationConnectionReference.webSocketClient.setTextMessageListener(this::onTextMessage);
                notificationConnectionReference.connect();
            }catch (Throwable t){
                log.severe(t.toString());
            }
        }

        public void close() {
            isWaitingToDie = true;
            notificationConnectionReference.close();
        }

    }

    private static class NotificationConnectionReference {

        private int id;

        private WebSocketClient webSocketClient;
        private NotificationAPIImpl notificationAPI;
        private boolean opened;

        public NotificationConnectionReference(NotificationAPIImpl notificationAPI, int id, WebSocketClient webSocketClient) {
            this.notificationAPI = notificationAPI;
            this.id = id;
            this.webSocketClient = webSocketClient;
        }

        public int getId() {
            return id;
        }

        public boolean isOpened(){
            return opened;
        }

        public NotificationConnectionReference connect() {
            try {
                webSocketClient.connect();
                opened = true;
                log.finest("notification reference created succeed");
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                opened = false;
                log.warning("unable to connect notification reference");
            }
            return this;
        }

        public void close() {
            webSocketClient.close();
            notificationAPI.remove(getId());
        }
    }

}
