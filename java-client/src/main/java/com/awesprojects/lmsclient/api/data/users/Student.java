package com.awesprojects.lmsclient.api.data.users;

import com.awesprojects.lmsclient.api.internal.Responsable;
import org.json.JSONObject;

import java.io.Serializable;

public class Student extends User implements Responsable,Serializable {

    public Student(JSONObject object){
        super(object);
        setType(1);
    }

    public Student(){
        super();
        setType(1);
    }



}
