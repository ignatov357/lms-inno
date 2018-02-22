package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.internal.Responsed;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;

@ToString
public class Response implements Responsed {

    @Getter @Setter
    private int status;
    @Getter @Setter
    private String description;

    public static final int STATUS_OK = 200;
    public static final int STATUS_ACCESS_TOKEN_ERROR = 401;
    public static final int STATUS_BAD_REQUEST_ERROR = 400;

    public Response(int status){
        this(status,"standart for "+status+" response code");
    }

    public Response(int status,String description){
        this.status = status;
        this.description = description;
    }

    public boolean isSuccess(){
        return status==0 ? true : false;
    }

    public boolean isError(){
        return !isSuccess();
    }

    public boolean isAccessTokenError(){
        return status==STATUS_ACCESS_TOKEN_ERROR;
    }

    public boolean isBadRequestError(){
        return status==STATUS_BAD_REQUEST_ERROR;
    }




    public static int getResultCode(String httpResponse){
        char[] stat = new char[3];
        int i = 0;
        for (;httpResponse.charAt(i)!=' ';i++);
        i++;
        char c = ' ';
        for (int j = 0;(c=httpResponse.charAt(i+j))!=' ';j++){
            stat[j]=c;
        }
        return Integer.parseInt(String.copyValueOf(stat));
    }

    public static Response getResult(String httpResponse){
        int resultCode = getResultCode(httpResponse);
        JSONObject object = getJsonBody(httpResponse);
        if (object.has("errorMessage")){
            return new Response(resultCode,object.getString("errorMessage"));
        }else{
            return new Response(resultCode);
        }
    }

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

}
