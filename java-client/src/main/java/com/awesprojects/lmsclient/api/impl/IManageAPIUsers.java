package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.Responsable;

@ApiImplementation(api = ManageAPI.Users.class)
public interface IManageAPIUsers {

    Responsable addUser(AccessToken accessToken, User user);

    Responsable modifyUser(AccessToken accessToken, User user);

    Responsable removeUser(AccessToken accessToken, User user);

    Responsable generateNewPassword(AccessToken accessToken, int userID);

    Responsable getDocumentsUserCheckedOut(AccessToken accessToken, int userId);

    Responsable getUser(AccessToken accessToken, int id);

    Responsable getUsers(AccessToken accessToken);

}
