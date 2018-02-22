package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;

public class UsersAPI {

    @ApiCall
    public static Responsable getAccessToken(int id, String password){
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/users/getAccessToken");
        request.withData("userID",id+"");
        request.withData("password",password);
        String response = request.create().execute();
        Response response1 = Response.getResult(response);
        if (response1.getStatus()==Response.STATUS_OK) {
            AccessToken accessToken = AccessToken.parse(Response.getJsonBody(response));
            return accessToken;
        }else{
            return response1;
        }
    }

    @ApiCall
    public static Responsable getAccessTokenUsingCardUID(String cardUID){
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/users/getAccessTokenUsingCardUID");
        request.withData("cardUID",cardUID);
        String response = request.create().execute();
        Response response1 = Response.getResult(response);
        if (response1.getStatus()==Response.STATUS_OK){
            AccessToken accessToken = AccessToken.parse(Response.getJsonBody(response));
            return accessToken;
        }else{
            return response1;
        }
    }

    @ApiCall
    public static Responsable changePassword(AccessToken accessToken,String newPassword){
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/users/changePassword");
        request.withHeader("Access-Token",accessToken.getToken());
        request.withData("newPassword",newPassword);
        String response = request.create().execute();
        Response response1 = Response.getResult(response);
        return response1;
    }

}
