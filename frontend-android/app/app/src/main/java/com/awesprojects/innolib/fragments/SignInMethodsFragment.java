package com.awesprojects.innolib.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.managers.SignInManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ilya on 2/23/18.
 */

public class SignInMethodsFragment extends Fragment implements View.OnClickListener,CheckBox.OnCheckedChangeListener{



    public interface Callback{
        void onSignInMethodsSet();
    }

    protected View mContent;
    protected CheckBox mCardUseCheckBox;
    protected CheckBox mFingerprintUseCheckBox;
    protected CheckBox mPinUseCheckBox;
    protected Button mBackButton;
    protected Button mApplyButton;
    protected TextView mNfcDoesNotSupportedTextView;
    protected TextView mFingerprintDoesNotSupportedTextView;
    protected TextView mInfoTextView;
    protected Callback mCallbackListener;

    boolean supportsNFC = false;
    boolean supportsFingerprint = false;

    public void setCallbackListener(Callback callback){
        mCallbackListener = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getActivity().getLayoutInflater().inflate(R.layout.fragment_signing_in_methods,null);
        mCardUseCheckBox = mContent.findViewById(R.id.fragment_signin_nfc_enable_checkbox);
        mCardUseCheckBox.setOnCheckedChangeListener(this);
        mFingerprintUseCheckBox = mContent.findViewById(R.id.fragment_signin_fingerprint_enable_checkbox);
        mFingerprintUseCheckBox.setOnCheckedChangeListener(this);
        mPinUseCheckBox = mContent.findViewById(R.id.fragment_signin_pin_enable_checkbox);
        mPinUseCheckBox.setOnCheckedChangeListener(this);
        mBackButton = mContent.findViewById(R.id.fragment_signin_methods_back_button);
        mBackButton.setOnClickListener(this);
        mApplyButton = mContent.findViewById(R.id.fragment_signin_methods_apply_button);
        mApplyButton.setOnClickListener(this);
        mInfoTextView = mContent.findViewById(R.id.fragment_signin_info_textview);
        mNfcDoesNotSupportedTextView = mContent.findViewById(R.id.fragment_signin_nfc_does_not_supported_textview);
        mFingerprintDoesNotSupportedTextView = mContent.findViewById(R.id.fragment_signin_fingerprint_does_not_supported_textview);
        supportsNFC = SignInManager.getInstance().isDeviceSupportsNfc(getActivity());
        supportsFingerprint = SignInManager.getInstance().isDeviceSupportsFingerprint(getActivity());

        if (!supportsFingerprint){
            mFingerprintDoesNotSupportedTextView.setVisibility(View.VISIBLE);
            mFingerprintUseCheckBox.setEnabled(false);
        }else{
            mFingerprintUseCheckBox.setChecked(true);
        }
        if (!supportsNFC){
            mNfcDoesNotSupportedTextView.setVisibility(View.VISIBLE);
            mCardUseCheckBox.setEnabled(false);
        }else{
            mCardUseCheckBox.setChecked(true);
        }
        mPinUseCheckBox.setChecked(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean saveChanges(){
        boolean useCard = mCardUseCheckBox.isChecked();
        boolean useFingerprint = mFingerprintUseCheckBox.isChecked();
        boolean usePin = mPinUseCheckBox.isChecked();
        boolean securityEnabled = usePin | useFingerprint | useCard ;
        SharedPreferences sp = getActivity().getSharedPreferences(InnolibApplication.PREFERENCES_SIGNIN_METHODS,MODE_PRIVATE);
        sp.edit().putBoolean("sign_in_using_card",useCard)
                .putBoolean("sign_in_using_fingerprint",useFingerprint)
                .putBoolean("sign_in_using_pin",usePin)
                .putBoolean("identity_check",securityEnabled)
                .commit();
        SharedPreferences appStatePrefs = getActivity().getSharedPreferences(
                InnolibApplication.PREFERENCES_APPLICATION_STATE,MODE_PRIVATE);
        return securityEnabled;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_signin_methods_back_button:{
                getActivity().onBackPressed();
            }
            case R.id.fragment_signin_methods_apply_button:{
                boolean securityEnabled = saveChanges();
                if (mCallbackListener!=null)
                    mCallbackListener.onSignInMethodsSet();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton==mPinUseCheckBox){
            TransitionManager.beginDelayedTransition((ViewGroup)mContent);
            if (b){
                mFingerprintUseCheckBox.setEnabled(true);
                mCardUseCheckBox.setEnabled(true);
                mFingerprintUseCheckBox.setChecked(true && supportsFingerprint);
                mCardUseCheckBox.setChecked(true && supportsNFC);
                mInfoTextView.setVisibility(View.INVISIBLE);
            }else{
                mFingerprintUseCheckBox.setChecked(false);
                mCardUseCheckBox.setChecked(false);
                mFingerprintUseCheckBox.setEnabled(false);
                mCardUseCheckBox.setEnabled(false);
                if (supportsNFC || supportsFingerprint)
                    mInfoTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
