package com.awesprojects.lmsclient.api;

import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.CheckOutInfo;
import com.awesprojects.lmsclient.api.data.documents.Article;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.users.User;
import com.awesprojects.lmsclient.api.internal.Responsable;
import com.awesprojects.lmsclient.utils.Config;
import com.awesprojects.lmsclient.utils.requests.GetRequest;
import com.awesprojects.lmsclient.utils.requests.PostRequest;
import com.awesprojects.lmsclient.utils.requests.RequestFactory;
import org.json.JSONArray;
import org.json.JSONObject;

public class ManageAPI {

    public static class Users{

        private static void writeUserIntoRequest(PostRequest.Builder request,User user){
            request.withData("id",user.getId()+"");
            request.withData("name",user.getName());
            request.withData("address",user.getAddress());
            request.withData("phone",user.getPhone());
            request.withData("type",user.getType()+"");
        }

        public static Responsable addUser(AccessToken accessToken,User user){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/users/addUser");
            writeUserIntoRequest(request,user);
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                JSONObject idpwd = Response.getJsonBody(response);
                user.setId(idpwd.getInt("id"));
                user.setPassword(idpwd.getString("password"));
                return user;
            }else{
                return Response.getResult(response);
            }
        }

        public static Responsable modifyUser(AccessToken accessToken,User user){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/users/modifyUser");
            writeUserIntoRequest(request,user);
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                return User.parseUser(Response.getJsonBody(response));
            }else{
                return Response.getResult(response);
            }
        }

        public static Responsable removeUser(AccessToken accessToken,User user){
            return removeUser(accessToken,user.getId());
        }

        public static Responsable removeUser(AccessToken accessToken,int userID){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/users/modifyUser");
            request.withData("id",userID+"");
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                User ret = User.parseUser(Response.getJsonBody(response));
                ret.setId(userID);
                return ret;
            }else{
                return Response.getResult(response);
            }
        }


        public static Responsable generateNewPassword(AccessToken accessToken,int userID){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/users/generateNewPassword");
            request.withData("id",userID+"");
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                User toWrite = User.getImpl();
                JSONObject idpwd = Response.getJsonBody(response);
                toWrite.setId(idpwd.getInt("id"));
                toWrite.setPassword(idpwd.getString("password"));
                return toWrite;
            }else{
                return Response.getResult(response);
            }
        }

        public static Responsable[] getDocumentsUserCheckedOut(AccessToken accessToken,int userId){
            GetRequest.Builder request = RequestFactory.get();
            request.withURL("/manage/users/getDocumentsUserCheckedOut");
            request.withQuery("user_id",userId+"");
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                JSONArray docsArray = Response.getJsonArrayBody(response);
                Document[] documents = new Document[docsArray.length()];
                for (int i = 0;i<docsArray.length();i++){
                    JSONObject obj = docsArray.getJSONObject(i);
                    JSONObject documentInfo = obj.getJSONObject("documentInfo");
                    documents[i] = Document.parseDocument(documentInfo);
                    CheckOutInfo check = CheckOutInfo.parseInfo(obj);
                    documents[i].setCheckOutInfo(check);
                }
                return documents;
            }else{
                return new Responsable[]{Response.getResult(response)};
            }
        }

        public static Responsable getUser(AccessToken accessToken,int id){
            GetRequest.Builder request = RequestFactory.get();
            request.withURL("/manage/users/getUser");
            request.withQuery("id",id+"");
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                return User.parseUser(Response.getJsonBody(response));
            }else{
                return Response.getResult(response);
            }
        }

        public static Responsable[] getUsers(AccessToken accessToken){
            GetRequest.Builder request = RequestFactory.get();
            request.withURL("/manage/users/getUsers");
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK){
                JSONArray usersArray = Response.getJsonArrayBody(response);
                User[] users = new User[usersArray.length()];
                for (int i = 0;i<usersArray.length();i++){
                    users[i] = User.parseUser(usersArray.getJSONObject(i));
                }
                return users;
            }else{
                return new Responsable[]{Response.getResult(response)};
            }
        }

    }

    public static class Documents{

        private static void writeDocumentIntoRequest(PostRequest.Builder request,Document document){
            request.withData("id",document.getId()+"");
            request.withData("instockCount",document.getInstockCount()+"");
            request.withData("type",document.getType()+"");
            request.withData("title",document.getTitle());
            request.withData("authors",document.getAuthors());
            request.withData("price", document.getPrice());
            request.withData("keywords",document.getKeywords());
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
            }catch(ClassCastException cce){
                Config.getCurrentConfig().getErr().println("class cast exception. wrong type of document?");
                throw cce;
            }
        }

        public static Responsable addDocument(AccessToken accessToken,Document document){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/documents/addDocument");
            writeDocumentIntoRequest(request,document);
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK) {
                return Document.parseDocument(Response.getJsonBody(response));
            }else{
                return Response.getResult(response);
            }
        }

        public static Responsable modifyDocument(AccessToken accessToken,Document document){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/documents/modifyDocument");
            writeDocumentIntoRequest(request,document);
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK) {
                // Due to wrong api response use this temporary solution
                // return Document.parseDocument(Response.getJsonBody(response));
                return document;
            }else{
                return Response.getResult(response);
            }
        }

        public static Responsable removeDocument(AccessToken accessToken,Document document){
            return removeDocument(accessToken,document.getId());
        }

        public static Responsable removeDocument(AccessToken accessToken,int documentID){
            PostRequest.Builder request = RequestFactory.post();
            request.withURL("/manage/documents/removeDocument");
            request.withData("id",documentID+"");
            request.withHeader("Access-Token",accessToken.getToken());
            String response = request.create().execute();
            if (Response.getResultCode(response)==Response.STATUS_OK) {
                // Due to wrong api response use this temporary solution
                 return Document.parseDocument(Response.getJsonBody(response));
                //return document;
            }else{
                return Response.getResult(response);
            }
        }

    }

}
