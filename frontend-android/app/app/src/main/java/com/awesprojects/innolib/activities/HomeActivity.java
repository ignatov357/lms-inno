package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.interfaces.HomeInterfaceFactory;
import com.awesprojects.innolib.interfaces.AbstractHomeInterface;
import com.awesprojects.innolib.managers.SecureStorageManager;
import com.awesprojects.innolib.services.NotificationService;
import com.awesprojects.lmsclient.api.data.AccessToken;
import com.awesprojects.lmsclient.api.data.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ilya on 2/4/18.
 */

public class HomeActivity extends Activity {

    public interface KeyDispatchListener{
        boolean onKeyEvent(KeyEvent event);
    }

    protected User mCurrentUser;
    protected AbstractHomeInterface mHomeInterface;
    protected List<KeyDispatchListener> mKeyListeners;

    public HomeActivity(){
        super();
        mKeyListeners = new ArrayList<>();
    }

    public User getUser(){
        return mCurrentUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (InnolibApplication.getAccessToken()==null){
            InnolibApplication.loadCachedToken();
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

    public void addKeyDispatchListener(KeyDispatchListener listener){
        mKeyListeners.add(listener);
    }

    public void removeKeyDispatchListener(KeyDispatchListener listener){
        if (mKeyListeners.contains(listener)) {
            mKeyListeners.remove(listener);
        }
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        for (KeyDispatchListener kdl : mKeyListeners){
            if (kdl.onKeyEvent(event)) return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        for (KeyDispatchListener kdl : mKeyListeners){
            if (kdl.onKeyEvent(event)) return true;
        }
        return super.onKeyDown(keyCode, event);
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
