package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

class DocumentsAPIImpl implements IDocumentsAPI {

    public Responsable getDocuments() {
        return getDocuments(false);
    }

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

    public Response checkOutDocument(String accessToken, int documentID) {
        PostRequest.Builder builder = RequestFactory.post();
        builder.withURL("/documents/checkOutDocument");
        builder.withHeader("Access-Token", accessToken);
        builder.withData("documentID", documentID + "");
        String response = builder.create().execute();
        return Response.getResult(response);
    }

    public Response renewDocument(String accessToken, int documentID) {
        PostRequest.Builder builder = RequestFactory.post();
        builder.withURL("/documents/renewDocument");
        builder.withHeader("Access-Token", accessToken);
        builder.withData("documentID", documentID + "");
        String response = builder.create().execute();
        return Response.getResult(response);
    }

}
