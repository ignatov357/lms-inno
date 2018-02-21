package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class DocumentsAPI {

    @ApiCall
    public static Document[] getDocuments(){
        GetRequest.Builder builder = RequestFactory.get();
        builder.withURL("/documents/getDocuments");
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

}
