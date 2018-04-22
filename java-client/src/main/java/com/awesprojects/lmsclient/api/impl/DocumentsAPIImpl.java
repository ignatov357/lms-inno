package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

class DocumentsAPIImpl implements IDocumentsAPI {

    public Responsable getDocuments() {
        return getDocuments(false);
    }

    @Override
    public Responsable getDocuments(boolean availableOnly) {
        GetRequest.Builder builder = RequestFactory.get();
        builder.withURL("/documents/getDocuments");
        if (availableOnly)
            builder.withQuery("availableOnly", "1");
        String response = builder.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            JSONArray documentsArray = Response.getJsonArrayBody(response);
            Document[] documents = new Document[documentsArray.length()];
            for (int i = 0; i < documents.length; i++) {
                JSONObject document = documentsArray.getJSONObject(i);
                Document doc = Document.parseDocument(document);
                documents[i] = doc;
            }
            return new ResponsableContainer<>(documents);
        } else {
            return Response.getResult(response);
        }
    }

    @Override
    public Responsable getDocument(int documentId) {
        GetRequest.Builder builder = RequestFactory.get();
        builder.withURL("/documents/getDocument");
        builder.withQuery("id", documentId + "");
        String response = builder.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            JSONObject document = Response.getJsonBody(response);
            return Document.parseDocument(document);
        } else {
            return Response.getResult(response);
        }
    }

    @Override
    public Responsable searchDocument(AccessToken accessToken,String searchQuery) {
        GetRequest.Builder builder = RequestFactory.get();
        //TODO: remove workaround
        builder.withURL("/documents/getDocuments");
        //builder.withHeader("Access-Token",accessToken.getToken());
        builder.withQuery("availableOnly","0");
        String response = builder.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK){
            JSONArray array = Response.getJsonArrayBody(response);
            ArrayList<Document> documentArrayList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Document document = Document.parseDocument(array.getJSONObject(i));
                if (document.getTitle().startsWith(searchQuery)){
                    documentArrayList.add(document);
                }
            }
            Document[] documents = new Document[documentArrayList.size()];
            documentArrayList.toArray(documents);
            return new ResponsableContainer<>(documents);
        }else{
            return Response.getResult(response);
        }
    }

    @Override
    public Response checkOutDocument(String accessToken, int documentID) {
        PostRequest.Builder builder = RequestFactory.post();
        builder.withURL("/documents/checkOutDocument");
        builder.withHeader("Access-Token", accessToken);
        builder.withData("documentID", documentID + "");
        String response = builder.create().execute();
        return Response.getResult(response);
    }

    @Override
    public Response renewDocument(String accessToken, int documentID) {
        PostRequest.Builder builder = RequestFactory.post();
        builder.withURL("/documents/renewDocument");
        builder.withHeader("Access-Token", accessToken);
        builder.withData("documentID", documentID + "");
        String response = builder.create().execute();
        return Response.getResult(response);
    }

}
