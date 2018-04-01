package com.awesprojects.lmsclient.api.data.users;

import com.awesprojects.lmsclient.api.internal.Responsable;
import org.json.JSONObject;

import java.io.Serializable;

public class Professor extends User implements Responsable,Serializable {

    public Professor(JSONObject object){
        super(object);
        setType(3);
    }

    public Professor(){
        super();
        setType(3);
    }

}
