package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeOverlayFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.data.documents.Document;

/**
 * Created by Ilya on 3/20/2018.
 */

public class DocumentInfoFragment extends AbstractHomeOverlayFragment implements View.OnClickListener{

    public interface OnCheckoutResultListener {
        void onResult(int status, Document document, String reason);
    }

    Document mDocument;
    ImageView mPreviewImageView;
    Toolbar mToolbar;
    TextView mAuthorTextView;
    TextView mStockTextView;
    TextView mEditionTextView;
    TextView mKeywordsTextView;
    Button mCheckoutButton;
    int mStockColorNormal;
    int mColorRed;
    int mColorYellow;
    LibraryCheckoutConfirmFragment.OnCheckoutResultListener mResultListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null)
            savedInstanceState = getArguments();
        mDocument = (Document) savedInstanceState.getSerializable("DOCUMENT");

        setContentView(R.layout.fragment_document_info);
        mColorYellow = getResources().getColor(R.color.colorDarkYellow);
        mColorRed = getResources().getColor(R.color.colorDarkRed);
        mToolbar = getContentView().findViewById(R.id.fragment_document_detail_toolbar);
        mAuthorTextView = getContentView().findViewById(R.id.fragment_document_detail_authors_textview);
        mKeywordsTextView = getContentView().findViewById(R.id.fragment_document_detail_keywords_textview);
        mPreviewImageView = getContentView().findViewById(R.id.fragment_document_detail_preview_imageview);
        mEditionTextView = getContentView().findViewById(R.id.fragment_document_detail_edition_textview);
        mCheckoutButton = getContentView().findViewById(R.id.fragment_document_detail_checkout_button);
        mStockTextView = getContentView().findViewById(R.id.fragment_document_detail_instock_textview);
        setupDocument(mDocument);
        getContentView().requestApplyInsets();
       // hideHomeUI(true);
    }

    public void setupDocument(Document document){
        setDocumentTitle(document.getTitle());
        mStockTextView.setText("1234536773723");
        setStockCount(document.getInstockCount());
        mAuthorTextView.setText(document.getAuthors());
        setKeywords(preparedKeywords(document));
        mPreviewImageView.setImageResource(R.drawable.ic_library_books_black_36dp);
        if (document instanceof Book){
            mEditionTextView.setText(((Book) document).getEdition()+" edition");
        }else{
            mEditionTextView.setVisibility(View.GONE);
        }
        if (document.getInstockCount()==0){
            mCheckoutButton.setText("Request for a "+
                    DocumentManager.getInstance().getDocumentType(document.getType(),getHomeActivity()).toLowerCase());
        }
        mCheckoutButton.setOnClickListener(this);
    }

    public void setOnCheckoutListener(LibraryCheckoutConfirmFragment.OnCheckoutResultListener listener){
        mResultListener = listener;
    }

    public void setDocumentTitle(String title){
        mToolbar.setTitle(title);
    }

    @Override
    public void onDestroy() {
        //showHomeUI(true);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("DOCUMENT",mDocument);
        super.onSaveInstanceState(outState);
    }

    public void setKeywords(String[] keywords) {
        if (keywords == null || keywords.length == 0) {
            mKeywordsTextView.setVisibility(View.GONE);
            mKeywordsTextView.setText("");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keywords.length; i++) {
            sb.append("#").append(keywords[i]);
            if (i != keywords.length - 1)
                sb.append(" ");
        }
        mKeywordsTextView.setVisibility(View.VISIBLE);
        mKeywordsTextView.setText(sb.toString());
    }

    public String[] preparedKeywords(Document document) {
        String keywords = document.getKeywords();
        if (keywords == null) return null;
        keywords = keywords.replace(" ", "_").replace(",_", ",");
        if (keywords.contains(","))
            return keywords.split(",");
        else
            return new String[]{keywords};
    }

    public void setStockCount(int count) {
        if (count == 0) {
           // mStockTextView.setTextColor(mColorRed);
            mStockTextView.setText(R.string.home_library_list_document_stock_empty);
        } else if (count == 1) {
           // mStockTextView.setTextColor(mColorYellow);
            mStockTextView.setText(R.string.home_library_list_document_stock_last);
        } else {
            String text = mStockTextView.getResources().getString(R.string.home_library_list_document_stock_default);
           // mStockTextView.setTextColor(mStockColorNormal);
            mStockTextView.setText(count + " " + text);
        }
    }

    @Override
    public void onClick(View view) {
        checkOut();
    }

    public void onCheckOutSucceed() {
        Toast.makeText(getActivity(),"operation succeed",Toast.LENGTH_LONG).show();
        if (mResultListener != null)
            mResultListener.onResult(0, mDocument, null);
    }

    public void onCheckOutFailed(String desc) {
        Toast.makeText(getActivity(),desc,Toast.LENGTH_LONG).show();
    }

    public void checkOut(){
        DocumentManager.checkOutDocumentAsync(InnolibApplication.getAccessToken(), mDocument.getId(),
                (responsable) -> {
                    if (responsable instanceof Response) {
                        int status = ((Response) responsable).getStatus();
                        if (status == 200) {
                            onCheckOutSucceed();
                        } else {
                            onCheckOutFailed(responsable.toString());
                        }
                    }
                });

    }
}
