package com.awesprojects.lmsclient.api.data;

import lombok.Data;
import org.json.JSONObject;

import java.io.Serializable;

@Data
public class CheckOutInfo implements Serializable {

    boolean overdue = false;

    int fine = 0;

    long returnTill;

    public static CheckOutInfo parseInfo(JSONObject object){
        CheckOutInfo checkOutInfo = new CheckOutInfo();
        checkOutInfo.setOverdue(object.optBoolean("isOverdue",false));
        checkOutInfo.setFine(object.optInt("fine",0));
        checkOutInfo.setReturnTill(object.optInt("returnTill",-1));
        return checkOutInfo;
    }
}
