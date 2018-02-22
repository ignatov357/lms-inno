package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.api.internal.Responsed;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;

public class UsersAPI {

    @ApiCall
    public static Responsed getAccessToken(int id, String password){
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

}
