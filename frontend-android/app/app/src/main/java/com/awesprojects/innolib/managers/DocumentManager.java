package com.awesprojects.innolib.managers;

import android.content.Context;
import android.os.AsyncTask;

import com.awesprojects.lmsclient.api.DocumentsAPI;
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

    public static void getDocumentAsync(OnGetDocumentsListener onGetDocumentsListener){
        getDocumentAsync(0,-1,onGetDocumentsListener);
    }

    public static void getDocumentAsync(int start, int count,OnGetDocumentsListener onGetDocumentsListener){
        GetDocumentAsyncTask getDocumentAsyncTask = new GetDocumentAsyncTask();
        getDocumentAsyncTask.setListener(onGetDocumentsListener);
        getDocumentAsyncTask.execute(new Integer[]{start,count});
    }
    public interface OnGetDocumentsListener {
        public void onDocumentGet(Document[] documents);
    }
    private static class GetDocumentAsyncTask extends AsyncTask<Integer[],Integer,Document[]>{

        OnGetDocumentsListener onGetDocumentsListener;

        public void setListener(OnGetDocumentsListener listener){
            onGetDocumentsListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document[] doInBackground(Integer[]... integers) {
            return DocumentsAPI.getDocuments();
        }

        @Override
        protected void onPostExecute(Document[] documents) {
            super.onPostExecute(documents);
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

}
