package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.impl.APIFactory;
import com.awesprojects.lmsclient.api.impl.ApiCallHandler;
import com.awesprojects.lmsclient.api.impl.IDocumentsAPI;
import com.awesprojects.lmsclient.api.internal.*;

import java.util.logging.Logger;

public class DocumentsAPI {

    public static final String NAME = "DocumentsAPI";
    private static final Logger log = Logger.getLogger(NAME);
    private static IDocumentsAPI impl;

    static {
        impl = APIFactory.getDefault().getDocumentsAPI();
    }


    @ApiCall(
            path = "/documents/getDocuments",
            method = Method.GET
    )
    @ResponseType(returns = {
            ResponsableContainer.class, //ResponsableContainer<Document[]>
            Response.class
    })
    public static Responsable getDocuments() {
        return impl.getDocuments(false);
    }

    @ApiCall(
            path = "/documents/getDocuments",
            method = Method.GET
    )
    @ResponseType(returns = {
            ResponsableContainer.class, //ResponsableContainer<Document[]>
            Response.class
    })
    public static Responsable getDocuments(boolean availableOnly) {
        return impl.getDocuments(availableOnly);
    }

    @ApiCall(
            //TODO: change path
            path = "/documents/getDocuments",
            method = Method.GET
    )
    @ResponseType(returns = {
            ResponsableContainer.class,
            Response.class
    })
    public static Responsable searchDocuments(AccessToken accessToken,String searchQuery,
                                              Search.Type type,Search.Where where,Search.Availability availability) {
        return impl.searchDocument(accessToken,searchQuery,type,where,availability);
    }

    @ApiCall(
            path = "/documents/getDocument",
            method = Method.GET
    )
    @ResponseType(returns = {
            Document.class,
            Response.class
    })
    public static Responsable getDocument(int documentId) {
        return impl.getDocument(documentId);
    }

    @ApiCall(
            path = "/documents/checkOutDocument",
            method = Method.POST
    )
    @ResponseType(returns = {
            Response.class
    })
    public static Response checkOutDocument(String accessToken, int documentID) {
        return impl.checkOutDocument(accessToken, documentID);
    }

    @ApiCall(
            path = "/documents/renewDocument",
            method = Method.POST
    )
    @ResponseType(returns = {
            Response.class
    })
    public static Response renewDocument(String accessToken, int documentID) {
        return impl.renewDocument(accessToken, documentID);
    }


}
