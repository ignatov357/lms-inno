package com.awesprojects.lmsclient.api.data;

import com.awesprojects.lmsclient.api.internal.Responsed;
import com.awesprojects.lmsclient.utils.Config;
import lombok.Data;
import org.json.JSONObject;
import java.util.Calendar;

@Data
public class AccessToken implements Responsed {

    public AccessToken(String accessToken,long expirationDate){
        this.token = accessToken;
        this.expirationDate = expirationDate;
    }

    private String token;

    private long expirationDate;

    public static AccessToken parse(JSONObject object){
        return new AccessToken(object.getString("accessToken"),object.optLong("expirationDate",-1));
    }

    public String getExpirationDateInString(){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(expirationDate*1000);
        StringBuilder sb = new StringBuilder();
        dumpCalendar(c,sb);
        if (Config.getCurrentConfig().isDebug()) {
            sb.append("(").append(expirationDate * 1000).append(")");
            sb.append(";  now");
            c.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
            dumpCalendar(c, sb);
            sb.append("(").append(Calendar.getInstance().getTimeInMillis()).append(")");
        }
        return sb.toString();
    }

    private void dumpCalendar(Calendar c,StringBuilder sb){
        int s = c.get(Calendar.SECOND);
        int m = c.get(Calendar.MINUTE);
        int h = c.get(Calendar.HOUR_OF_DAY);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        sb.append(day<10?"0"+day:day+"")
                .append(".")
                .append(month<10?"0"+month:month)
                .append(".")
                .append((year % 10000) + "")
                .append(" ")
                .append(h<10 ? "0"+h : h)
                .append(":")
                .append(m<10 ? "0"+m : m)
                .append(":")
                .append(s<10 ? "0"+s : s);
    }

}
