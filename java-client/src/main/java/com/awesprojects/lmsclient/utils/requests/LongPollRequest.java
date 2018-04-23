package com.awesprojects.lmsclient.utils.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class LongPollRequest extends GetRequest{

    Socket socket;
    int port = 80;

    public LongPollRequest(){
        super();
        setCloseConnection(false);
    }

    public InputStream open(){
        StringBuilder sb = new StringBuilder();
        super.buildRequest(sb);
        RequestExecutor.AbstractApiRequest apiRequest =  RequestExecutor.createApiRequest(sb.toString(),port);
        try {
            apiRequest.connect();
            socket = apiRequest.getSocket();
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public static class Builder extends AbstractBuilder<Builder,LongPollRequest>{

        private LongPollRequest request;

        Builder(){
            super();
            request = new LongPollRequest();
            returns(this,request);
        }

        public Builder withQuery(String name, String value){
            request.query(name,value);
            return this;
        }

        @Override
        public Builder withHeader(String key, String value) {
            withHeaderInternal(key,value);
            return this;
        }

        public Builder onPort(int port){
            request.port = port;
            return this;
        }

    }

}
