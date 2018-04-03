package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeOverlayFragment;

public class SignOutConfirmFragment extends AbstractHomeOverlayFragment{

    public interface SignOutConfirmCallback{
        void signOutConfirmed(boolean confirmed);
    }

    SignOutConfirmCallback mSignOutConfirmCallback;
    Button mOKButton;
    Button mCancelButton;
    View mTouchOutsideView;

    public SignOutConfirmFragment() {
        super();
    }

    public void setCallbackListener(SignOutConfirmCallback callback){
        mSignOutConfirmCallback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_signout_confirm);
        mOKButton = getContentView().findViewById(R.id.fragment_signout_confirm_ok_button);
        mCancelButton = getContentView().findViewById(R.id.fragment_signout_confirm_cancel_button);
        mTouchOutsideView = getContentView().findViewById(R.id.fragment_signout_confirm_cancel_outside_view);
        mOKButton.setOnClickListener(this::onClick);
        mCancelButton.setOnClickListener(this::onClick);
        mTouchOutsideView.setOnClickListener(this::onClick);
    }


    public void onClick(View view) {
        if (mSignOutConfirmCallback==null) return;
        if (view==mOKButton){
            mSignOutConfirmCallback.signOutConfirmed(true);
        }else{
            mSignOutConfirmCallback.signOutConfirmed(false);
        }
        getFragmentManager().beginTransaction()
                .remove(this)
                .commit();
    }

}
