package com.awesprojects.lmsclient.api.data.documents;


import org.json.JSONObject;

public class RemovedDocument extends Document{

    public RemovedDocument(){
        super();
        setId(-1);
        setTitle("Removed document");
        setAuthors("unknown");
        setType(-1);
        setPrice("0");
        setInstockCount(0);
        setKeywords("removed");
    }

    public RemovedDocument(JSONObject object){
        this();
    }

}
