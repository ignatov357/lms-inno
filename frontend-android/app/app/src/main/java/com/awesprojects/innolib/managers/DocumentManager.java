package com.awesprojects.innolib.managers;

import android.content.Context;
import android.os.AsyncTask;

import com.awesprojects.innolib.R;
import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.UsersAPI;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.internal.Responsable;

import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by ilya on 3/1/18.
 */

public class DocumentManager {

    public static final String TAG = "DocumentManager";
    private static final Logger log = Logger.getLogger(TAG);

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
        void onDocumentGet(Responsable documents);
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

    public static void renewDocumentAsync(AccessToken accessToken,int documentId,
                                          OnDocumentRenewListener renewListener){
        RenewDocumentAsyncTask asyncTask = new RenewDocumentAsyncTask();
        asyncTask.setListener(renewListener);
        asyncTask.execute(accessToken,documentId);
    }

    public interface OnDocumentRenewListener {
        void onDocumentRenewed(Responsable responsable);
    }

    private static class RenewDocumentAsyncTask extends AsyncTask<Object,Integer,Responsable>{

        OnDocumentRenewListener onDocumentRenewListener;

        public void setListener(OnDocumentRenewListener listener){
            onDocumentRenewListener = listener;
        }
        @Override
        protected Responsable doInBackground(Object... objects) {
            String token = ((AccessToken)objects[0]).getToken();
            int id = (int) objects[1];
            return DocumentsAPI.renewDocument(token,id);
        }

        @Override
        protected void onPostExecute(Responsable responsable) {
            super.onPostExecute(responsable);
            onDocumentRenewListener.onDocumentRenewed(responsable);
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

    public static String getPrettyReturnDate(long seconds){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(seconds*1000);
        StringBuilder sb = new StringBuilder();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR) % 100;
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        sb.append("Return till ");
        sb.append(day).append(".").append(month<10 ? "0"+month : month).append(".").append(year < 10 ? "0"+year : year);
        sb.append(" at ").append(hour).append(":").append(minute<10 ? "0"+minute : minute);
        return sb.toString();
    }

}
