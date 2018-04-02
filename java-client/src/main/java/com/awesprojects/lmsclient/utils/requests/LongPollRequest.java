package com.awesprojects.lmsclient.utils.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class LongPollRequest extends GetRequest{

    Socket socket;

    public LongPollRequest(){
        super();
        setCloseConnection(false);
    }

    public InputStream open(){
        StringBuilder sb = new StringBuilder();
        super.buildRequest(sb);
        RequestExecutor.AbstractApiRequest apiRequest =  RequestExecutor.createApiRequest(sb.toString());
        try {
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

    }

}
