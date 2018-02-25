package com.awesprojects.innolib.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.managers.SecureStorageManager;
import com.awesprojects.innolib.managers.SignInManager;
import com.awesprojects.innolib.utils.SignInHandler;
import com.awesprojects.lmsclient.api.data.AccessToken;

/**
 * Created by ilya on 2/4/18.
 */

public class SignInFragment extends Fragment implements View.OnClickListener,SignInHandler.SignInResult{

    public interface OnSignInListener{
        public void onSignIn(AccessToken accessToken);
    }

    ViewGroup mContent;
    LinearLayout mCenterUiContainer;
    Button mSignInButton;
    EditText mUserIdEditText;
    EditText mUserPasswordEditText;
    ProgressBar mSignInProgressBar;
    TextView mResponseInfo;
    boolean isSigningIn=false;
    OnSignInListener mSignInListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_signin,null);
        mCenterUiContainer = (LinearLayout) mContent.findViewById(R.id.fragment_signin_center_ui_container);
        mResponseInfo = mContent.findViewById(R.id.fragment_signin_response_info_textview);
        mSignInButton = mContent.findViewById(R.id.fragment_signin_signin_button);
        mSignInButton.setOnClickListener(this);
        mUserIdEditText = mContent.findViewById(R.id.fragment_signin_userid_edittext);
        mUserPasswordEditText = mContent.findViewById(R.id.fragment_signin_userpassword_edittext);
        mSignInProgressBar = mContent.findViewById(R.id.fragment_signin_progress_bar);
        mSignInProgressBar.setIndeterminate(true);
    }

    public void setOnSignInListener(OnSignInListener onSignInListener){
        this.mSignInListener = onSignInListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_signin_signin_button:{
                startSigningIn(mUserIdEditText.getText().toString(),mUserPasswordEditText.getText().toString());
                break;
            }
        }
    }

    public void onSignInSucceed(AccessToken accessToken){
        //Toast.makeText(getActivity(),accessToken.toString(),Toast.LENGTH_LONG).show();
        SecureStorageManager.getInstance().beginTransaction()
                .put("USER_ID",mUserIdEditText.getText().toString())
                .put("USER_PASSWORD",mUserPasswordEditText.getText().toString())
                .put("ACCESS_TOKEN",accessToken.getToken())
                .put("ACCESS_TOKEN_INSPIRATION_DATE",accessToken.getExpirationDate()+"")
                .commit();
        if (mSignInListener!=null){
            mSignInListener.onSignIn(accessToken);
        }
    }

    public void onSignInResult(Message msg){
        isSigningIn = false;
        TransitionManager.beginDelayedTransition(mCenterUiContainer);
        if (msg.what!=200){
            mUserIdEditText.setEnabled(true);
            mUserPasswordEditText.setEnabled(true);
        }
        switch (msg.what){
            case -1:{
                //mResponseInfo.setVisibility(View.VISIBLE);
                mResponseInfo.setText(R.string.signin_info_wrong_userid_form);
                break;
            }
            case 400:{
                //mResponseInfo.setVisibility(View.VISIBLE);
                mResponseInfo.setText(R.string.signin_info_wrong_userid_or_password);
                break;
            }
            case 200:{
                onSignInSucceed((AccessToken)msg.obj);
                mResponseInfo.setText("");
                //mResponseInfo.setVisibility(View.GONE);
                break;
            }
            default:{
                //mResponseInfo.setVisibility(View.VISIBLE);
                mResponseInfo.setText(R.string.signin_info_unknown_error);
            }
        }
        mSignInProgressBar.setVisibility(View.INVISIBLE);
        mSignInButton.setText(R.string.signin);
    }

    public void startSigningIn(String id,String password){
        startUISigningIn();
        startApiSigningIn(id,password);
    }

    public void startApiSigningIn(String id,String password){
        SignInManager.getInstance().getSignInHandler().attach(this);
        SignInManager.getInstance().startApiSigningIn(id,password);
    }

    public void startUISigningIn(){
        TransitionManager.beginDelayedTransition(mCenterUiContainer);
        mSignInButton.setText(R.string.signin_cancel);
        mUserIdEditText.setEnabled(false);
        mUserPasswordEditText.setEnabled(false);
        //mSignInButton.setVisibility(View.GONE);
        mSignInProgressBar.setVisibility(View.VISIBLE);
        isSigningIn = true;
    }



}
