package com.awesprojects.lmsclient.api.data.users;

import org.json.JSONObject;

public class TeacherAssistant extends User {

    public TeacherAssistant(JSONObject object){
        super(object);
        setType(2);
    }

    public TeacherAssistant(){
        super();
        setType(2);
    }

}
