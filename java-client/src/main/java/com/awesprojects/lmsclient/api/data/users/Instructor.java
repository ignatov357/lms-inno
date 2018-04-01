package com.awesprojects.lmsclient.api.data.users;

import org.json.JSONObject;

public class Instructor extends User {

    public Instructor(JSONObject object){
        super(object);
        setType(4);
    }

    public Instructor(){
        super();
        setType(4);
    }

}
