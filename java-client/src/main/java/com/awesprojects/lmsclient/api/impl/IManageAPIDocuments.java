package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.ManageAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;

@ApiImplementation(api = ManageAPI.Documents.class)
public interface IManageAPIDocuments {
    Responsable addDocument(AccessToken accessToken, Document document);

    Responsable modifyDocument(AccessToken accessToken, Document document);

    Responsable removeDocument(AccessToken accessToken, Document document);
}
