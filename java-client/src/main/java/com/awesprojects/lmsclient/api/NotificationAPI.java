package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.utils.requests.LongPollRequest;

import java.util.HashMap;
import java.util.logging.Logger;

public class NotificationAPI {


    public static final String NAME = "DocumentsAPI";
    private static final Logger log = Logger.getLogger(NAME);

    static {
        log.fine("document api log configured");
    }

    private static HashMap<ConnectionReference,LongPollRequest> connections;

    public static void create(LongPollRequest request){
        ConnectionReference connectionReference = new ConnectionReference(request.hashCode());
        connections.put(connectionReference,request);
    }


    private static class ConnectionReference{

        private int mId;

        public ConnectionReference(int id){
            mId = id;
        }

        public int getId(){
            return mId;
        }
    }

}
