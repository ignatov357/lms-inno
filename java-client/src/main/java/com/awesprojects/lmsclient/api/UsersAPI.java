package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;

public class UsersAPI {

    @ApiCall
    public static String getAccessToken(int id,String password){
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/apimethod");
        request.withData("userID",id+"");
        request.withData("password",password);
        String response = request.create().execute();
        return null;
    }

}
