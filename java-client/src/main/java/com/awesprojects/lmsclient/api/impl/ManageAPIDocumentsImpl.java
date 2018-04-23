package com.awesprojects.lmsclient.api.impl;

import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Article;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.Config;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;

class ManageAPIDocumentsImpl implements IManageAPIDocuments {

    private static void writeDocumentIntoRequest(PostRequest.Builder request, Document document) {
        if (document.getId() != -1)
            request.withData("id", document.getId() + "");
        request.withData("instockCount", document.getInstockCount() + "");
        request.withData("type", document.getType() + "");
        request.withData("title", document.getTitle());
        request.withData("authors", document.getAuthors());
        request.withData("price", document.getPrice());
        request.withData("keywords", document.getKeywords());
        try {
            switch (document.getType()) {
                case 0: {
                    Book book = (Book) document;
                    request.withData("reference", "" + (book.isReference() ? 1 : 0));
                    request.withData("bestseller", "" + (book.isBestseller() ? 1 : 0));
                    request.withData("publisher", book.getPublisher());
                    request.withData("edition", book.getEdition() + "");
                    request.withData("publicationYear", book.getPublicationYear() + "");
                    break;
                }
                case 1: {
                    Article article = (Article) document;
                    request.withData("journalTitle", article.getJournalTitle());
                    request.withData("journalIssuePublicationDate", article.getJournalIssuePublicationDate());
                    request.withData("journalIssueEditors", article.getJournalIssueEditors());
                    break;
                }
            }
        } catch (ClassCastException cce) {
            Config.getCurrentConfig().getErr().println("class cast exception. wrong type of document?");
            throw cce;
        }
    }

    public Responsable addDocument(AccessToken accessToken, Document document) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/documents/addDocument");
        writeDocumentIntoRequest(request, document);
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            return Document.parseDocument(Response.getJsonBody(response));
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable modifyDocument(AccessToken accessToken, Document document) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/documents/modifyDocument");
        writeDocumentIntoRequest(request, document);
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            // Due to wrong api response use this temporary solution
            // return Document.parseDocument(Response.getJsonBody(response));
            return document;
        } else {
            return Response.getResult(response);
        }
    }

    public Responsable removeDocument(AccessToken accessToken, Document document) {
        return removeDocument(accessToken, document.getId());
    }

    public Responsable removeDocument(AccessToken accessToken, int documentID) {
        PostRequest.Builder request = RequestFactory.post();
        request.withURL("/manage/documents/removeDocument");
        request.withData("id", documentID + "");
        request.withHeader("Access-Token", accessToken.getToken());
        String response = request.create().execute();
        if (Response.getResultCode(response) == Response.STATUS_OK) {
            // Due to wrong api response use this temporary solution
            return Document.parseDocument(Response.getJsonBody(response));
            //return document;
        } else {
            return Response.getResult(response);
        }
    }


}
