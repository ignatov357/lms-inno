package com.awesprojects.lmsclient.api.data;

import lombok.Data;
import org.json.JSONObject;

import java.io.Serializable;

@Data
public class CheckOutInfo implements Serializable {

    boolean overdue = false;

    String fine = "0.00";

    long returnTill;

    public static CheckOutInfo parseInfo(JSONObject object){
        CheckOutInfo checkOutInfo = new CheckOutInfo();
        checkOutInfo.setOverdue(object.optBoolean("isOverdue",false));
        checkOutInfo.setFine(object.optString("fine","0.00"));
        checkOutInfo.setReturnTill(object.optInt("returnTill",-1));
        return checkOutInfo;
    }
}
