package com.awesprojects.lmsclient.utils.sockets;

import com.awesprojects.lmsclient.utils.Config;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import com.neovisionaries.ws.client.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class WebSocketClient {

    public interface TextMessageListener {
        void onTextMessage(String message);
    }

    public interface ConnectionStateListener{
        void onConnected();
        void onDisconnected();
    }

    private String address;
    private int port;
    private String url;
    private WebSocket socket;
    private TextMessageListener textMessageListener;
    private ConnectionStateListener connectionStateListener;

    public WebSocketClient(String address, int port, String url) throws IOException {
        this.address = address;
        this.port = port;
        this.url = url;
        socket = new WebSocketFactory().createSocket("ws://" + address+":"+port+url);
    }

    public void setTextMessageListener(TextMessageListener listener) {
        textMessageListener = listener;
    }

    public void setConnectionStateListener(ConnectionStateListener connectionStateListener) {
        this.connectionStateListener = connectionStateListener;
    }

    private void addListener() {
        if (socket == null) return;
        socket.addListener(new WebSocketAdapter() {
            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                super.onConnected(websocket, headers);
                if (connectionStateListener!=null)
                    connectionStateListener.onConnected();
            }

            @Override
            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                super.onTextMessage(websocket, text);
                System.out.println("text received: "+text);
                if (textMessageListener != null)
                    textMessageListener.onTextMessage(text);
            }

            @Override
            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
                if (connectionStateListener!=null)
                    connectionStateListener.onDisconnected();
            }
        });
    }

    public void connect() throws WebSocketException {
        addListener();
        socket.connect();
    }

    public void close() {
        try {
            //socket.sendClose();
            socket.disconnect();
        } catch (Throwable throwable) {

        }
    }

}
