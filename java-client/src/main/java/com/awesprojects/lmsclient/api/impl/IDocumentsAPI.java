package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.Search;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

@ApiImplementation(api = DocumentsAPI.class)
public interface IDocumentsAPI {

    Responsable getDocuments();

    Responsable getDocuments(boolean availableOnly);

    Responsable getDocument(int documentId);

    Responsable searchDocument(AccessToken accessToken, String searchQuery,
                               Search.Type type, Search.Where where, Search.Availability availability);

    Response checkOutDocument(String accessToken, int documentID);

    Response renewDocument(String accessToken, int documentID);
}