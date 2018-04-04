package com.awesprojects.lmsclient.api.data.users;

import com.awesprojects.lmsclient.api.internal.Responsable;
import org.json.JSONObject;

import java.io.Serializable;

public class Librarian extends User implements Responsable,Serializable {

    public Librarian(JSONObject object){
        super(object);
        setType(0);
    }

    public Librarian(){
        super();
        setType(0);
    }

}
