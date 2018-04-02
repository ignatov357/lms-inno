package com.awesprojects.lmsclient.api.data.users;

import com.awesprojects.lmsclient.api.internal.Responsable;
import org.json.JSONObject;

import java.io.Serializable;

public class VisitingProfessor extends User implements Serializable,Responsable{

    public VisitingProfessor(JSONObject object){
        super(object);
        setType(5);
    }

    public VisitingProfessor(){
        super();
        setType(5);
    }

}
