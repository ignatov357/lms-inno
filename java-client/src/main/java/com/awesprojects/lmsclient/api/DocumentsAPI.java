package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class DocumentsAPI {

    @ApiCall
    public static Document[] getDocuments(){
        return getDocuments(false);
    }

    @ApiCall
    public static Document[] getDocuments(boolean availableOnly){
        GetRequest.Builder builder = RequestFactory.get();
        builder.withURL("/documents/getDocuments");
        if (availableOnly)
            builder.withQuery("availableOnly","1");
        String response = builder.create().execute();
        JSONArray documentsArray = Response.getJsonArrayBody(response);
        Document[] documents = new Document[documentsArray.length()];
        for (int i = 0;i<documents.length;i++) {
            JSONObject document = documentsArray.getJSONObject(i);
            Document doc = Document.parseDocument(document);
            documents[i] = doc;
        }
        return documents;
    }

    @ApiCall
    public static Document getDocument(int documentId){
        GetRequest.Builder builder = RequestFactory.get();
        builder.withURL("/documents/getDocument");
        builder.withQuery("id",documentId+"");
        String response = builder.create().execute();
        JSONObject document = Response.getJsonBody(response);
        return Document.parseDocument(document);
    }

    public static Response checkOutDocument(String accessToken,int documentID){
        PostRequest.Builder builder = RequestFactory.post();
        builder.withURL("/documents/checkOutDocument");
        builder.withHeader("Access-Token",accessToken);
        builder.withData("documentID",documentID+"");
        String response = builder.create().execute();
        return Response.getResult(response);
    }

}
