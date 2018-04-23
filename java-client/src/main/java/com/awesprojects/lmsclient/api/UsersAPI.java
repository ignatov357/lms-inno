package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.impl.APIFactory;
import com.awesprojects.lmsclient.api.impl.ApiCallHandler;
import com.awesprojects.lmsclient.api.impl.IUsersAPI;
import com.awesprojects.lmsclient.api.internal.*;

public class UsersAPI {


    private static IUsersAPI impl;

    static {
        impl = APIFactory.getDefault().getUsersAPI();
    }

    @ApiCall(
            path = "/users/getAccessToken",
            method = Method.POST
    )
    @ResponseType(returns = {
            AccessToken.class,
            Response.class
    })
    public static Responsable getAccessToken(int id, String password) {
        return impl.getAccessToken(id, password);
    }

    @ApiCall(
            path = "/users/getAccessTokenUsingCardUID",
            method = Method.POST
    )
    @ResponseType(returns = {
            AccessToken.class,
            Response.class
    })
    public static Responsable getAccessTokenUsingCardUID(String cardUID) {
        return impl.getAccessTokenUsingCardUID(cardUID);
    }

    @ApiCall(
            path = "/users/changePassword",
            method = Method.POST
    )
    @ResponseType(returns = {
            Response.class
    })
    public static Responsable changePassword(AccessToken accessToken, String newPassword) {
        return impl.changePassword(accessToken, newPassword);
    }

    @ApiCall(
            path = "/users/getUserInfo",
            method = Method.GET
    )
    @ResponseType(returns = {
            User.class,
            Response.class
    })
    public static Responsable getUserInfo(AccessToken accessToken) {
        return impl.getUserInfo(accessToken);
    }

    @ApiCall(
            path = "/users/getCheckedOutDocuments",
            method = Method.GET
    )
    @ResponseType(returns = {
            ResponsableContainer.class, //ResponsableContainer<Document[]>
            Response.class
    })
    public static Responsable getCheckedOutDocuments(AccessToken accessToken) {
        return impl.getCheckedOutDocuments(accessToken);
    }


}
