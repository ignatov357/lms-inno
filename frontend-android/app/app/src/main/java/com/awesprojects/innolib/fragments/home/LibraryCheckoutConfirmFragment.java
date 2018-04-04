package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeOverlayFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.Response;
import com.awesprojects.lmsclient.api.data.documents.Document;

/**
 * Created by ilya on 3/4/18.
 */

public class LibraryCheckoutConfirmFragment extends AbstractHomeOverlayFragment implements View.OnClickListener {

    public interface OnCheckoutResultListener {
        void onResult(int status, Document document, String reason);
    }

    public LibraryCheckoutConfirmFragment() {
        super();
        setEnterTransition(new Slide(Gravity.BOTTOM));
        setExitTransition(new Slide(Gravity.BOTTOM));
    }

    Document mDocument;
    TextView mDocumentDescription;
    Button mCancelButton;
    Button mConfirmButton;
    View mOutsideCancelView;
    OnCheckoutResultListener mResultListener;

    public void setResultListener(OnCheckoutResultListener listener) {
        mResultListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            mDocument = (Document) getArguments().getSerializable("DOCUMENT");
        else
            mDocument = (Document) savedInstanceState.getSerializable("DOCUMENT");
        setContentView(R.layout.fragment_home_checkout_confirm);
        mDocumentDescription = getContentView().findViewById(R.id.fragment_checkout_confirm_document_description);
        mDocumentDescription.setText(mDocument.toString());
        mOutsideCancelView = getContentView().findViewById(R.id.fragment_checkout_confirm_cancel_outside_view);
        mCancelButton = getContentView().findViewById(R.id.fragment_checkout_confirm_cancel_button);
        mConfirmButton = getContentView().findViewById(R.id.fragment_checkout_confirm_confirm_button);
        mOutsideCancelView.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("DOCUMENT", mDocument);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void checkOut() {
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


    public void onCheckOutSucceed() {
        getFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        if (mResultListener != null)
            mResultListener.onResult(0, mDocument, null);
    }

    public void onCheckOutFailed(String desc) {
        mDocumentDescription.setText(desc);
    }

    @Override
    public void onClick(View view) {
        if (view == mConfirmButton) {
            checkOut();
        } else {
            setExitTransition(new Fade(Fade.OUT));
            getFragmentManager().beginTransaction()
                    .remove(this)
                    .commit();
        }
    }
}
