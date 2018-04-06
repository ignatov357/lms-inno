package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.CheckOutInfo;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

class ManageAPIUsersImpl implements IManageAPIUsers {

    private void writeUserIntoRequest(PostRequest.Builder request, User user) {
        request.withData("id", user.getId() + "");
        request.withData("name", user.getName());
        request.withData("address", user.getAddress());
        request.withData("phone", user.getPhone());
        request.withData("type", user.getType() + "");
    }

    public Responsable addUser(AccessToken accessToken, User user) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/users/addUser");
        writeUserIntoRequest(request, user);
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            JSONObject idpwd = Response.getJsonBody(response);
            user.setId(idpwd.getInt("id"));
            user.setPassword(idpwd.getString("password"));
            return user;
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable modifyUser(AccessToken accessToken, User user) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/users/modifyUser");
        writeUserIntoRequest(request, user);
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            return User.parseUser(Response.getJsonBody(response));
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable removeUser(AccessToken accessToken, User user) {
        return removeUser(accessToken, user.getId());
    }

    public Responsable removeUser(AccessToken accessToken, int userID) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/users/modifyUser");
        request.withData("id", userID + "");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            User ret = User.parseUser(Response.getJsonBody(response));
            ret.setId(userID);
            return ret;
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable generateNewPassword(AccessToken accessToken, int userID) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/users/generateNewPassword");
        request.withData("id", userID + "");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            User toWrite = User.getImpl();
            JSONObject idpwd = Response.getJsonBody(response);
            toWrite.setId(idpwd.getInt("id"));
            toWrite.setPassword(idpwd.getString("password"));
            return toWrite;
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable getDocumentsUserCheckedOut(AccessToken accessToken, int userId) {
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/manage/users/getDocumentsUserCheckedOut");
        request.withQuery("user_id", userId + "");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            JSONArray docsArray = Response.getJsonArrayBody(response);
            Document[] documents = new Document[docsArray.length()];
            for (int i = 0; i < docsArray.length(); i++) {
                JSONObject obj = docsArray.getJSONObject(i);
                JSONObject documentInfo = obj.getJSONObject("documentInfo");
                documents[i] = Document.parseDocument(documentInfo);
                CheckOutInfo check = CheckOutInfo.parseInfo(obj);
                documents[i].setCheckOutInfo(check);
            }
            return new ResponsableContainer<>(documents);
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable getUser(AccessToken accessToken, int id) {
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/manage/users/getUser");
        request.withQuery("id", id + "");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            return User.parseUser(Response.getJsonBody(response));
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable getUsers(AccessToken accessToken) {
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/manage/users/getUsers");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            JSONArray usersArray = Response.getJsonArrayBody(response);
            User[] users = new User[usersArray.length()];
            for (int i = 0; i < usersArray.length(); i++) {
                users[i] = User.parseUser(usersArray.getJSONObject(i));
            }
            return new ResponsableContainer<>(users);
        } else {
            return Response.getResult(response);
        }
    }

}
