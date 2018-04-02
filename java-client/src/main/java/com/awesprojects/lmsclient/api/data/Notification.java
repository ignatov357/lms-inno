package com.awesprojects.lmsclient.api.data;

import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Data;
import org.json.JSONObject;

import java.io.Serializable;

@Data
public class Notification implements Responsable,Serializable {

    private int id;

    private String description;

    public static Notification parse(JSONObject jsonObject){
        Notification notification = new Notification();
        notification.setId(jsonObject.optInt("notificationID",0));
        notification.setDescription(jsonObject.optString("notification",""));
        return notification;
    }

}
