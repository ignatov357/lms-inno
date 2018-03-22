package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.awesprojects.innolib.interfaces.HomeInterfaceFactory;
import com.awesprojects.innolib.interfaces.AbstractHomeInterface;
import com.awesprojects.innolib.utils.logger.ActivityLogger;
import com.awesprojects.lmsclient.api.data.users.User;

/**
 * Created by ilya on 2/4/18.
 */

public class HomeActivity extends Activity {

    protected User mCurrentUser;
    protected AbstractHomeInterface mHomeInterface;
    protected ActivityLogger mActivityLogger;

    public User getUser(){
        return mCurrentUser;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (savedInstanceState==null)
            mActivityLogger = ActivityLogger.create(this);
        else {
            mActivityLogger = (ActivityLogger) savedInstanceState.getSerializable("ACTIVITY_LOGGER");
            mActivityLogger.attach(this);
        }

        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        mCurrentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        mHomeInterface = HomeInterfaceFactory.createInterfaceByUserType(this,mCurrentUser.getType());
        mHomeInterface.create(savedInstanceState);
        mActivityLogger.onCreate(savedInstanceState);
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
        mActivityLogger.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("ACTIVITY_LOGGER",mActivityLogger);
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

    }

    public void showHomeUI(boolean animated){

    }

    public final ViewGroup getRootViewGroup(){
        return mHomeInterface.getRootViewGroup();
    }
}
