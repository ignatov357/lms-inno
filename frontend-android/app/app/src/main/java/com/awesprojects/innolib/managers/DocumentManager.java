package com.awesprojects.innolib.managers;

import android.content.Context;
import android.os.AsyncTask;

import com.awesprojects.innolib.R;
import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;

/**
 * Created by ilya on 3/1/18.
 */

public class DocumentManager {

    private static DocumentManager mInstance;

    public static DocumentManager getInstance() {
        if (mInstance==null)
            mInstance = new DocumentManager();
        return mInstance;
    }

    public static void getCheckedOutDocuments(AccessToken accessToken,OnGetDocumentsListener listener){
        GetCheckedOutDocuments getCheckedOutDocumentsAsyncTask = new GetCheckedOutDocuments();
        getCheckedOutDocumentsAsyncTask.setListener(listener);
        getCheckedOutDocumentsAsyncTask.execute(accessToken);
    }

    public static void getDocumentAsync(OnGetDocumentsListener onGetDocumentsListener){
        getDocumentAsync(false,onGetDocumentsListener);
    }

    public static void getDocumentAsync(boolean availableOnly,OnGetDocumentsListener onGetDocumentsListener){
        GetDocumentAsyncTask getDocumentAsyncTask = new GetDocumentAsyncTask();
        getDocumentAsyncTask.setListener(onGetDocumentsListener);
        getDocumentAsyncTask.execute(availableOnly);
    }
    public interface OnGetDocumentsListener {
        public void onDocumentGet(Responsable documents);
    }
    private static class GetDocumentAsyncTask extends AsyncTask<Boolean,Integer,Responsable>{
        OnGetDocumentsListener onGetDocumentsListener;
        public void setListener(OnGetDocumentsListener listener){
            onGetDocumentsListener = listener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Responsable doInBackground(Boolean ... availableOnly) {
            return DocumentsAPI.getDocuments(availableOnly[0]);
        }
        @Override
        protected void onPostExecute(Responsable documents) {
            super.onPostExecute(documents);
            onGetDocumentsListener.onDocumentGet(documents);
        }
    }


    private static class GetCheckedOutDocuments extends AsyncTask<AccessToken,Integer,Responsable>{
        OnGetDocumentsListener onGetDocumentsListener;
        public void setListener(OnGetDocumentsListener listener){
            onGetDocumentsListener = listener;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Responsable doInBackground(AccessToken ... accessToken) {
            return UsersAPI.getCheckedOutDocuments(accessToken[0]);
        }
        @Override
        protected void onPostExecute(Responsable documents) {
            super.onPostExecute(documents);
            if (documents instanceof ResponsableContainer)
                onGetDocumentsListener.onDocumentGet(documents);
        }
    }


    public static void checkOutDocumentAsync(AccessToken accessToken,int documentId,
                                             OnDocumentCheckOutListener checkOutListener){
        CheckOutDocumentAsyncTask asyncTask = new CheckOutDocumentAsyncTask();
        asyncTask.setListener(checkOutListener);
        asyncTask.execute(accessToken,documentId);
    }
    public interface OnDocumentCheckOutListener {
        void onDocumentCheckedOut(Responsable responsable);
    }
    private static class CheckOutDocumentAsyncTask extends AsyncTask<Object,Integer,Responsable>{

        OnDocumentCheckOutListener onDocumentCheckOutListener;

        public void setListener(OnDocumentCheckOutListener listener){
            onDocumentCheckOutListener = listener;
        }
        @Override
        protected Responsable doInBackground(Object... objects) {
            String token = ((AccessToken)objects[0]).getToken();
            int id = (int) objects[1];
            return DocumentsAPI.checkOutDocument(token,id);
        }

        @Override
        protected void onPostExecute(Responsable responsable) {
            super.onPostExecute(responsable);
            onDocumentCheckOutListener.onDocumentCheckedOut(responsable);
        }
    }

    boolean isStringLoaded=false;
    String mDocumentBook;
    String mDocumentArticle;
    String mDocumentAV;

    private void ensureStringsLoaded(Context context){
        if (!isStringLoaded){
            mDocumentBook = context.getResources().getString(R.string.document_0);
            mDocumentArticle = context.getResources().getString(R.string.document_1);
            mDocumentAV = context.getResources().getString(R.string.document_2);
        }
    }

    public String getDocumentType(int type,Context context){
        ensureStringsLoaded(context);
        switch (type){
            case 0: return mDocumentBook;
            case 1: return mDocumentArticle;
            case 2: return mDocumentAV;
            default: return "Unknown";
        }
    }

}
