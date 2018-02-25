package com.awesprojects.innolib.utils;

import android.os.Handler;
import android.os.Message;

import com.awesprojects.innolib.fragments.SignInFragment;

/**
 * Created by ilya on 2/25/18.
 */
public class SignInHandler extends Handler {

    public interface SignInResult{
        public void onSignInResult(Message msg);
    }

    SignInResult attached;

    public void attach(SignInResult fragment){
        attached = fragment;
    }

    @Override
    public void handleMessage(Message msg) {
        if (attached!=null){
            attached.onSignInResult(msg);
        }
    }
}
