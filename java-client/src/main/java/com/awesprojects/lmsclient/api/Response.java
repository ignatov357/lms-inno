package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.internal.Responsable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.json.JSONArray;
import org.json.JSONObject;

@ToString
public class Response implements Responsable {

    @Getter
    @Setter
    private int status;
    @Getter
    @Setter
    private String description;

    public static final int STATUS_OK = 200;
    public static final int STATUS_ACCESS_TOKEN_ERROR = 401;
    public static final int STATUS_BAD_REQUEST_ERROR = 400;
    public static final int STATUS_HOST_UNAVAILABLE = -2;
    public static final int STATUS_API_ERROR = -64;

    public Response(int status) {
        this(status, "standart for " + status + " response code");
    }

    public Response(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public boolean isSuccess() {
        return status == 0 ? true : false;
    }

    public boolean isError() {
        return !isSuccess();
    }

    public boolean isAccessTokenError() {
        return status == STATUS_ACCESS_TOKEN_ERROR;
    }

    public boolean isBadRequestError() {
        return status == STATUS_BAD_REQUEST_ERROR;
    }

    public boolean isHostResolveFailed() {
        return status == STATUS_HOST_UNAVAILABLE;
    }

    public boolean isApiError(){
        return status == STATUS_API_ERROR;
    }


    public static int getResultCode(String httpResponse) {
        if (httpResponse == null) return -1;
        if (httpResponse.length() == 0) return 0;
        char[] stat = new char[3];
        int i = 0;
        for (; httpResponse.charAt(i) != ' '; i++) ;
        i++;
        char c = ' ';
        for (int j = 0; (c = httpResponse.charAt(i + j)) != ' '; j++) {
            stat[j] = c;
        }
        return Integer.parseInt(String.copyValueOf(stat));
    }

    public static Response getResult(String httpResponse) {
        int resultCode = getResultCode(httpResponse);
        if (resultCode == -1)
            return new Response(STATUS_HOST_UNAVAILABLE, "Host unavailable");
        JSONObject object = getJsonBody(httpResponse);
        if (object == null)
            return new Response(resultCode);
        if (object.has("errorMessage")) {
            return new Response(resultCode, object.getString("errorMessage"));
        } else {
            return new Response(resultCode);
        }
    }

    public static String getBody(String httpResponse) {
        if (httpResponse == null) return null;
        if (httpResponse.contains("\n\n"))
            return httpResponse.split("\n\n", 2)[1];
        else
            return null;
    }

    public static JSONObject getJsonBody(String httpResponse) {
        String body = getBody(httpResponse);
        if (body == null || body.equals(""))
            return null;
        else
            return new JSONObject(body);
    }

    public static JSONArray getJsonArrayBody(String httpResponse) {
        return new JSONArray(getBody(httpResponse));
    }

}
