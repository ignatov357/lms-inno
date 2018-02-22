package com.awesprojects.lmsclient.api.data.users;

import com.awesprojects.lmsclient.api.internal.Responsable;
import org.json.JSONObject;

import java.io.Serializable;

public class Faculty extends User implements Responsable,Serializable {

    public Faculty(JSONObject object){
        super(object);
        setType(1);
    }

    public Faculty(){
        super();
        setType(1);
    }

}
