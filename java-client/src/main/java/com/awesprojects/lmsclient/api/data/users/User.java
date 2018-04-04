package com.awesprojects.lmsclient.api.data.users;

import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Data;
import lombok.ToString;
import org.json.JSONObject;

import java.io.Serializable;

@Data
@ToString(exclude = "password")
public abstract class User implements Responsable,Serializable {

    public User(){}

    public User(JSONObject object){
        id = object.optInt("id",id);
        name = object.optString("name","unknown");
        address = object.optString("address","unknown");
        phone = object.optString("phone","123456");
        type = object.optInt("type",0);
    }

    protected int id = -1;

    protected String name;

    protected String address;

    protected String phone;

    protected int type;

    protected transient String password;

    public static User parseUser(JSONObject object){
        if (!object.has("type"))
            throw new NullPointerException("unable to determine user type");
        switch (object.optInt("type")){
            case 0: return new Librarian(object);
            case 1: return new Student(object);
            case 2: return new TeacherAssistant(object);
            case 3: return new Professor(object);
            case 4: return new Instructor(object);
            case 5: return new VisitingProfessor(object);
        }
        throw new ArrayIndexOutOfBoundsException("unknown type : "+object.optInt("type"));
    }

    public static User getImpl(){
        return new UserImpl();
    }


    protected static class UserImpl extends User implements Responsable,Serializable{
        public UserImpl(JSONObject object){
            super(object);
            setType(-2);
        }

        public UserImpl(){
            super();
            setType(-2);
        }
    }

}
