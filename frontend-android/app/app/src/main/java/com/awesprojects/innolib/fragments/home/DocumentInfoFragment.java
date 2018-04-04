package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeOverlayFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.CheckOutInfo;
import com.awesprojects.lmsclient.api.data.documents.Article;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.documents.EMaterial;

import java.util.logging.Logger;

/**
 * Created by Ilya on 3/20/2018.
 */

public class DocumentInfoFragment extends AbstractHomeOverlayFragment implements View.OnClickListener{

    public static final String TAG = "DocInfoFragment";
    private static final Logger log = Logger.getLogger(TAG);

    public interface OnCheckoutResultListener {
        void onResult(int status, Document document, String reason);
    }

    public interface OnRenewResultListener{
        void onResult(Document document,boolean renewed);
    }

    Document mDocument;
    ImageView mPreviewImageView;
    View mBestsellerIndicator;
    Toolbar mToolbar;
    TextView mAuthorTextView;
    TextView mStockTextView;
    TextView mEditionTextView;
    TextView mKeywordsTextView;
    TextView mBottomInfoTextView;
    TextView mTopInfo1TextView;
    TextView mPriceTextView;
    Button mCheckoutButton;
    int mStockColorNormal;
    int mColorRed;
    int mColorYellow;
    boolean checkoutMode=true;
    OnCheckoutResultListener mCheckoutResultListener;
    OnRenewResultListener mRenewResultListener;

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
        mBestsellerIndicator = getContentView().findViewById(R.id.fragment_document_detail_bestseller_indicator);
        mAuthorTextView = getContentView().findViewById(R.id.fragment_document_detail_authors_textview);
        mKeywordsTextView = getContentView().findViewById(R.id.fragment_document_detail_keywords_textview);
        mPreviewImageView = getContentView().findViewById(R.id.fragment_document_detail_preview_imageview);
        mEditionTextView = getContentView().findViewById(R.id.fragment_document_detail_edition_textview);
        mTopInfo1TextView = getContentView().findViewById(R.id.fragment_document_detail_top_first_textview);
        mBottomInfoTextView = getContentView().findViewById(R.id.fragment_document_detail_bottom_info_textview);
        mCheckoutButton = getContentView().findViewById(R.id.fragment_document_detail_checkout_button);
        mStockTextView = getContentView().findViewById(R.id.fragment_document_detail_instock_textview);
        mPriceTextView = getContentView().findViewById(R.id.fragment_document_detail_price_textview);
        setupDocument(mDocument);
        setupCheckOutInfo(mDocument.getCheckOutInfo());
        getContentView().requestApplyInsets();
       // hideHomeUI(true);
    }

    public void setupDocument(Document document){
        setDocumentTitle(document.getTitle());
        mStockTextView.setText("1234536773723");
        setStockCount(document.getInstockCount());
        mAuthorTextView.setText("By "+document.getAuthors());
        setKeywords(preparedKeywords(document));
        mPreviewImageView.setImageResource(R.drawable.ic_library_books_black_36dp);
        mCheckoutButton.setEnabled(true);
        mTopInfo1TextView.setVisibility(View.GONE);
        mBottomInfoTextView.setVisibility(View.GONE);
        if (document.getPrice()!=null) {
            try {
                float price = Float.parseFloat(document.getPrice());
                if (price > 0)
                    mPriceTextView.setText("Price : "+document.getPrice());
            } catch (Throwable t) {
                log.info("unable to parse price for doc with id = "+document.getId());
                mPriceTextView.setText("Price : "+document.getPrice());
            }
        }
        if (document instanceof Book){
            mPreviewImageView.setImageResource(R.drawable.ic_library_books_black_36dp);
            mEditionTextView.setText(((Book) document).getEdition()+" edition");
            if (((Book) document).isReference()){
                mBottomInfoTextView.setVisibility(View.VISIBLE);
                mBottomInfoTextView.setText("This is a reference book");
                mCheckoutButton.setEnabled(false);
            }
            if (((Book) document).isBestseller()){
                mBestsellerIndicator.setVisibility(View.VISIBLE);
            }else{
                mBestsellerIndicator.setVisibility(View.INVISIBLE);
            }
        }else{
            mEditionTextView.setVisibility(View.GONE);
        }
        if (document instanceof Article){
            mPreviewImageView.setImageResource(R.drawable.ic_insert_drive_file_black_48dp);
            mEditionTextView.setVisibility(View.VISIBLE);
            mEditionTextView.setText("Published in " + ((Article) document).getJournalTitle() + " in " + ((Article) document).getJournalIssuePublicationDate());
            mTopInfo1TextView.setVisibility(View.VISIBLE);
            mTopInfo1TextView.setText("Issue edited by "+((Article) document).getJournalIssueEditors());
        }
        if (document instanceof EMaterial){
            mPreviewImageView.setImageResource(R.drawable.ic_video_library_black_48dp);
        }
        if (document.getInstockCount()==0){
            mCheckoutButton.setText("Request for a "+
                    DocumentManager.getInstance().getDocumentType(document.getType(),getHomeActivity()).toLowerCase());
        }
        mCheckoutButton.setOnClickListener(this);
    }

    public void setupCheckOutInfo(CheckOutInfo checkOutInfo){
        ViewGroup vg = getContentView().findViewById(R.id.fragment_document_detail_checkout_layout);
        if (checkOutInfo==null){
            vg.setVisibility(View.GONE);
            return;
        }
        TextView returnDate  = vg.findViewById(R.id.fragment_document_detail_return_date_textview);
        View overdueIndicator = vg.findViewById(R.id.fragment_document_detail_overdue_indicator);
        String returnAt = DocumentManager.getPrettyReturnDate(checkOutInfo.getReturnTill());
        returnDate.setText(returnAt);
        if (checkOutInfo.getReturnTill()*1000<System.currentTimeMillis())
            overdueIndicator.setVisibility(View.VISIBLE);
        else
            overdueIndicator.setVisibility(View.GONE);
        TextView currentFine  = vg.findViewById(R.id.fragment_document_detail_fine_textview);
        currentFine.setText("Current fine : "+checkOutInfo.getFine());
        mCheckoutButton.setText("RENEW");
        checkoutMode = false;
    }

    public void setOnCheckoutListener(OnCheckoutResultListener listener){
        mCheckoutResultListener = listener;
    }

    public void setOnRenewListener(OnRenewResultListener listener){
        mRenewResultListener = listener;
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
        if (checkoutMode)
            checkOut();
        else
            renew();
    }

    public void onCheckOutSucceed() {
        Snackbar.make(getContentView(),"Checkout succeed",Snackbar.LENGTH_SHORT).show();
        if (mCheckoutResultListener != null)
            mCheckoutResultListener.onResult(0, mDocument, null);
    }

    public void onCheckOutFailed(String desc) {
        final Snackbar s = Snackbar.make(getContentView(),desc,Snackbar.LENGTH_LONG);
        s.setAction("OK",(view) -> {s.dismiss();});
        s.show();
    }

    public void onRenewSucceed(){
        Snackbar.make(getContentView(),"Renew succeed",Snackbar.LENGTH_SHORT).show();
        if (mRenewResultListener!=null)
            mRenewResultListener.onResult(mDocument,true);
    }

    public void onRenewFailed(String desc){
        final Snackbar s = Snackbar.make(getContentView(),desc,Snackbar.LENGTH_LONG);
        s.setAction("OK",(view) -> {s.dismiss();});
        s.show();
    }





    boolean savedCheckoutEnabledState;

    public void renew(){
        mCheckoutButton.setEnabled(false);
        DocumentManager.renewDocumentAsync(InnolibApplication.getAccessToken(),mDocument.getId(),
                (responsable) -> {
                    mCheckoutButton.setEnabled(true);
                    if (responsable instanceof Response) {
                        int status = ((Response) responsable).getStatus();
                        if (status == 200) {
                            onCheckOutSucceed();
                        } else {
                            onCheckOutFailed(((Response) responsable).getDescription());
                        }
                    }
                });
    }

    public void checkOut(){
        savedCheckoutEnabledState = mCheckoutButton.isEnabled();
        mCheckoutButton.setEnabled(false);
        DocumentManager.checkOutDocumentAsync(InnolibApplication.getAccessToken(), mDocument.getId(),
                (responsable) -> {
                    mCheckoutButton.setEnabled(savedCheckoutEnabledState);
                    if (responsable instanceof Response) {
                        int status = ((Response) responsable).getStatus();
                        if (status == 200) {
                            onCheckOutSucceed();
                        } else {
                            onCheckOutFailed(((Response) responsable).getDescription());
                        }
                    }
                });

    }
}
