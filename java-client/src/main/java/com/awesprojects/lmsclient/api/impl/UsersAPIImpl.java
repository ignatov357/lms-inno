package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.CheckOutInfo;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.documents.RemovedDocument;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

class UsersAPIImpl implements IUsersAPI {

    public static final String NAME = "UsersAPI";
    private static final Logger log = Logger.getLogger(NAME);

    public Responsable getAccessToken(int id, String password) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/users/getAccessToken");
        request.withData("userID", id + "");
        request.withData("password", password);
        String response = request.create().execute();
        Response response1 = Response.getResult(response);
        if (response1.getStatus() == Response.STATUS_OK) {
            log.info("access token received");
            AccessToken accessToken = AccessToken.parse(Response.getJsonBody(response));
            return accessToken;
        } else {
            return response1;
        }
    }

    public Responsable getAccessTokenUsingCardUID(String cardUID) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/users/getAccessTokenUsingCardUID");
        request.withData("cardUID", cardUID);
        String response = request.create().execute();
        Response response1 = Response.getResult(response);
        if (response1.getStatus() == Response.STATUS_OK) {
            AccessToken accessToken = AccessToken.parse(Response.getJsonBody(response));
            return accessToken;
        } else {
            return response1;
        }
    }

    public Responsable changePassword(AccessToken accessToken, String newPassword) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/users/changePassword");
        request.withHeader("Access-Token", accessToken.getToken());
        request.withData("newPassword", newPassword);
        String response = request.create().execute();
        Response response1 = Response.getResult(response);
        return response1;
    }

    public Responsable getUserInfo(AccessToken accessToken) {
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/users/getUserInfo");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == 200) {
            return User.parseUser(Response.getJsonBody(response));
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable getCheckedOutDocuments(AccessToken accessToken) {
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/users/getCheckedOutDocuments");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == 200) {
            JSONArray array = Response.getJsonArrayBody(response);
            int size = array.length();
            Document[] documents = new Document[size];
            for (int i = 0; i < size; i++) {
                JSONObject objI = array.getJSONObject(i);
                if (objI.isNull("documentInfo")) {
                    documents[i] = new RemovedDocument();
                } else {
                    documents[i] = Document.parseDocument(objI.getJSONObject("documentInfo"));
                }
                CheckOutInfo checkOutInfo = CheckOutInfo.parseInfo(objI);
                documents[i].setCheckOutInfo(checkOutInfo);
            }
            return new ResponsableContainer<>(documents);
        } else {
            return Response.getResult(response);
        }
    }

}
