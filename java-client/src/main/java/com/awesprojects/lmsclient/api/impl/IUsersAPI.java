package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

@ApiImplementation(api = UsersAPI.class)
public interface IUsersAPI {
    Responsable getAccessToken(int id, String password);

    Responsable getAccessTokenUsingCardUID(String cardUID);

    Responsable changePassword(AccessToken accessToken, String newPassword);

    Responsable getUserInfo(AccessToken accessToken);

    Responsable getCheckedOutDocuments(AccessToken accessToken);
}
