package com.awesprojects.lmsclient.api;

import org.json.JSONArray;
import org.json.JSONObject;

public class Response {

    public static String getBody(String httpResponse){
        if (httpResponse.contains("\n\n"))
            return httpResponse.split("\n\n",2)[1];
        else
            return null;
    }

    public static JSONObject getJsonBody(String httpResponse){
        return new JSONObject(getBody(httpResponse));
    }

    public static JSONArray getJsonArrayBody(String httpResponse){
        return new JSONArray(getBody(httpResponse));
    }

    public interface ApiResponse {
        public void onResponse(int code, String body);
    }

}
