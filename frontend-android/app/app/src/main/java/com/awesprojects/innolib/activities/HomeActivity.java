package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.interfaces.HomeInterfaceFactory;
import com.awesprojects.innolib.interfaces.AbstractHomeInterface;
import com.awesprojects.innolib.managers.SecureStorageManager;
import com.awesprojects.innolib.services.NotificationService;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;

/**
 * Created by ilya on 2/4/18.
 */

public class HomeActivity extends Activity {

    protected User mCurrentUser;
    protected AbstractHomeInterface mHomeInterface;

    public User getUser(){
        return mCurrentUser;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (InnolibApplication.getAccessToken()==null){
            String token = SecureStorageManager.getInstance().get("CACHED_ACCESS_TOKEN");
            AccessToken accessToken = new AccessToken(token,-1);
            InnolibApplication.setAccessToken(accessToken);
        }
        mCurrentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        mHomeInterface = HomeInterfaceFactory.createInterfaceByUserType(this,mCurrentUser.getType());
        mHomeInterface.create(savedInstanceState);
        Intent notificationServiceIntent = new Intent(this, NotificationService.class);
        startService(notificationServiceIntent);
        /*if (savedInstanceState==null){
            if (mCurrentUser.getType()==2)
                mHomeFragment = new LibrarianProfileFragment();
            else
                mHomeFragment = new PatronProfileFragment();
            mHomeFragment.setUser(mCurrentUser);
            mHomeFragment.setEnterTransition(new Fade(Fade.IN));
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_home_main_container,mHomeFragment,"HomeFragment")
                    .commit();
        }*/
    }

    protected void addHomeFragment(){

    }

    public AbstractHomeInterface getHomeInterface(){
        return mHomeInterface;
    }

    @Override
    protected void onDestroy() {
        mHomeInterface.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mHomeInterface.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHomeInterface.restoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeInterface.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHomeInterface.pause();
    }

    public void hideHomeUI(boolean animated){
        mHomeInterface.hideHomeUI(animated);
    }

    public void showHomeUI(boolean animated){
        mHomeInterface.showHomeUI(animated);
    }

    public final ViewGroup getRootViewGroup(){
        return mHomeInterface.getRootViewGroup();
    }
}
