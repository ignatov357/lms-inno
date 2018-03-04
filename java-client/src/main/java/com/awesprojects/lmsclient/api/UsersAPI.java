package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.CheckOutInfo;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

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

    @ApiCall
    public static Responsable getUserInfo(AccessToken accessToken){
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/users/getUserInfo");
        request.withHeader("Access-Token",accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response)==200){
            return User.parseUser(Response.getJsonBody(response));
        }else{
            return Response.getResult(response);
        }
    }

    @ApiCall
    public static Responsable getCheckedOutDocuments(AccessToken accessToken){
        GetRequest.Builder request = RequestFactory.get();
        request.withURL("/users/getCheckedOutDocuments");
        request.withHeader("Access-Token",accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response)==200){
            JSONArray array = Response.getJsonArrayBody(response);
            int size = array.length();
            Document[] documents = new Document[size];
            for (int i = 0; i < size; i++) {
                JSONObject objI = array.getJSONObject(i);
                documents[i] = Document.parseDocument(objI.getJSONObject("documentInfo"));
                CheckOutInfo checkOutInfo = CheckOutInfo.parseInfo(objI);
                documents[i].setCheckOutInfo(checkOutInfo);
            }
            return new ResponsableContainer<>(documents);
        }else {
            return Response.getResult(response);
        }
    }

}
