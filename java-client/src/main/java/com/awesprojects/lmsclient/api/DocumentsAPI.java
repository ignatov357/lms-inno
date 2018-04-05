package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.ApiCall;
import com.awesprojects.lmsclient.api.internal.ApiCallHandler;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

public class DocumentsAPI {

    public static final String NAME = "DocumentsAPI";
    private static final Logger log = Logger.getLogger(NAME);
    private static IDocumentsAPI impl;

    static {
        impl = ApiCallHandler.createAndWrap(new DocumentsAPIImpl(), IDocumentsAPI.class);
    }


    @ApiCall(path = "/documents/getDocuments")
    public static Responsable getDocuments() {
        return impl.getDocuments(false);
    }

    @ApiCall(path = "/documents/getDocuments")
    public static Responsable getDocuments(boolean availableOnly) {
        return impl.getDocuments(availableOnly);
    }

    @ApiCall(path = "/documents/getDocument")
    public static Responsable getDocument(int documentId) {
        return impl.getDocument(documentId);
    }

    @ApiCall(path = "/documents/checkOutDocument")
    public static Response checkOutDocument(String accessToken, int documentID) {
        return impl.checkOutDocument(accessToken, documentID);
    }

    @ApiCall(path = "/documents/renewDocument")
    public static Response renewDocument(String accessToken, int documentID) {
        return impl.renewDocument(accessToken, documentID);
    }


    private interface IDocumentsAPI {
        Responsable getDocuments();

        Responsable getDocuments(boolean availableOnly);

        Responsable getDocument(int documentId);

        Response checkOutDocument(String accessToken, int documentID);

        Response renewDocument(String accessToken, int documentID);
    }


    private static class DocumentsAPIImpl implements IDocumentsAPI {

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


}
