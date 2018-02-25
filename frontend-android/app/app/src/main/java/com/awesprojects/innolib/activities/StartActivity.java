package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.IdentityConfirmFragment;
import com.awesprojects.innolib.fragments.SignInFragment;
import com.awesprojects.innolib.fragments.SignInMethodsFragment;
import com.awesprojects.innolib.managers.SecureStorageManager;
import com.awesprojects.innolib.managers.SignInManager;
import com.awesprojects.innolib.managers.UserManager;
import com.awesprojects.innolib.utils.SignInHandler;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;

public class StartActivity extends Activity implements SignInFragment.OnSignInListener,
        SignInMethodsFragment.Callback,IdentityConfirmFragment.Callback,SignInHandler.SignInResult{

    public static final String PREFERENCE_IS_SIGNED_IN = "is_signed_in";
    public static final String PREFERENCE_IS_SIGNING_IN_METHODS_DEFINED = "is_signing_in_methods_defined";

    FrameLayout mContainer;
    SharedPreferences mPreferences;
    SharedPreferences mSignInPreferences;
    Fragment mCurrentFragment;
    SignInFragment mSignInFragment;
    SignInMethodsFragment mSignInMethodsFragment;
    IdentityConfirmFragment mIdentityConfirmFragment;
    AccessToken mAccessToken;
    boolean trainMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //int a =0;
        //a++; a--;
        //int b = a/a;
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences(InnolibApplication.PREFERENCES_APPLICATION_STATE,MODE_PRIVATE);
        mSignInPreferences = getSharedPreferences(InnolibApplication.PREFERENCES_SIGNIN_METHODS,MODE_PRIVATE);
        setContentView(R.layout.activity_start);
        mContainer = findViewById(R.id.activity_start_main_container);
        boolean isWelcomeScreenShown = mPreferences.getBoolean(WelcomeActivity.PREFERENCE_WELCOME_SCREEN_SHOWN,false);
        boolean isSignedIn = mPreferences.getBoolean(PREFERENCE_IS_SIGNED_IN,false);
        if (!isWelcomeScreenShown) {
            Intent welcomeActivityIntent = new Intent(this, WelcomeActivity.class);
            startActivity(welcomeActivityIntent);
        }
        if (!isSignedIn){
            startSigningIn();
        }else{
            checkSecurity(false);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    public void checkSecurity(boolean trainMode){
        this.trainMode = trainMode;
        if (mSignInPreferences.getBoolean("identity_check",false)){
            mIdentityConfirmFragment = new IdentityConfirmFragment();
            mIdentityConfirmFragment.setCallbackListener(this);
            if (trainMode) {
                mIdentityConfirmFragment.setEnterTransition(new Slide(Gravity.RIGHT));
                mIdentityConfirmFragment.setExitTransition(new Slide(Gravity.RIGHT));
            }
            mIdentityConfirmFragment.setTrainMode(trainMode);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (trainMode)
                ft.addToBackStack("IdentifyFragmentAdd");
            ft.add(R.id.activity_start_main_container,mIdentityConfirmFragment,"IdentityConfirmFragment");
            ft.commit();
            mCurrentFragment = mIdentityConfirmFragment;
        }else{
            onIdentityConfirmSuccess();
        }
    }

    public void startSigningIn(){
        mSignInMethodsFragment = new SignInMethodsFragment();
        mSignInMethodsFragment.setRetainInstance(false);
        mSignInMethodsFragment.setCallbackListener(this);
        getFragmentManager().beginTransaction()
                .add(R.id.activity_start_main_container, mSignInMethodsFragment,"SignInMethodsFragment")
                .commit();
        mSignInFragment = new SignInFragment();
        mSignInFragment.setRetainInstance(false);
        mSignInFragment.setEnterTransition(new Slide(Gravity.RIGHT));
        mSignInFragment.setExitTransition(new Slide(Gravity.LEFT));
        mSignInFragment.setReenterTransition(new Slide(Gravity.LEFT));
        mSignInFragment.setReturnTransition(new Slide(Gravity.LEFT));
        mSignInFragment.setOnSignInListener(this);
        getFragmentManager().beginTransaction()
                .add(R.id.activity_start_main_container,mSignInFragment,"SignInFragment")
                .commit();
        mCurrentFragment = mSignInFragment;
    }

    @Override
    public void onSignIn(AccessToken accessToken) {
        if (accessToken==null)
            return;
        mPreferences.edit().putBoolean("is_signed_in",true);
        InnolibApplication.setAccessToken(accessToken);
        getFragmentManager().beginTransaction()
                .remove(mSignInFragment)
                .addToBackStack("SignInExit")
                .commit();
        mCurrentFragment = mSignInMethodsFragment;
    }

    @Override
    public void onSignInMethodsSet() {
        checkSecurity(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (mCurrentFragment==mSignInMethodsFragment){
            getFragmentManager().beginTransaction()
                    .show(mSignInFragment)
                    .commit();
            mCurrentFragment = mSignInFragment;
        }*/
    }


    @Override
    public void onIdentityConfirmSuccess() {
        byte[] card = mIdentityConfirmFragment==null ? null : mIdentityConfirmFragment.getUserCardData();
        if (card==null) {
            if (InnolibApplication.getAccessToken()==null
                    || InnolibApplication.getAccessToken().getExpirationDate() < System.currentTimeMillis()) {
                validateAccessToken();
            }else{
                finishAllAndShowHome();
            }
        }

    }

    @Override
    public void onIdentityConfirmFail() {}

    public void validateAccessToken(){
        int userId = Integer.parseInt(SecureStorageManager.getInstance().get("USER_ID"));
        String password = SecureStorageManager.getInstance().get("USER_PASSWORD");
        SignInManager.getInstance().getSignInHandler().attach(this);
        SignInManager.getInstance().startApiSigningIn(userId+"",password);
    }

    @Override
    public void onSignInResult(Message msg) {
        if (msg.what==200){
            InnolibApplication.setAccessToken((AccessToken) msg.obj);
            finishAllAndShowHome();
        }
    }

    public void finishAllAndShowHome(){
        mPreferences.edit()
                .putBoolean(PREFERENCE_IS_SIGNED_IN,true)
                .commit();
        int userID = Integer.parseInt(SecureStorageManager.getInstance().get("USER_ID"));
        UserManager.getInstance().getUserAsync(InnolibApplication.getAccessToken(),userID,(responsable) -> {
            if (responsable instanceof User){
                runOnUiThread( () -> showHome((User)responsable) );
            }
        });
    }

    public void showHome(User user){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("CURRENT_USER",user);
        startActivity(intent);
        finish();
    }

}
