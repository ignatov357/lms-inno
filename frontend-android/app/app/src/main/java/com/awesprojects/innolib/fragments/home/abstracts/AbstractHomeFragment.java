package com.awesprojects.innolib.fragments.home.abstracts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.awesprojects.innolib.activities.HomeActivity;
import com.awesprojects.innolib.fragments.AbstractExtendedFragment;
import com.awesprojects.lmsclient.api.data.users.User;

import java.util.logging.Logger;

/**
 * Created by ilya on 2/25/18.
 */

public class AbstractHomeFragment extends AbstractExtendedFragment {

    public static final String TAG = "HomeFragment";
    public static Logger log = Logger.getLogger(TAG);

    private User mUser;
    private int menuId;

    public void setUser(User user){
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }

    public void setMenuId(int menuId){
        this.menuId = menuId;
    }

    public int getMenuId(){
        return menuId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity && !(context instanceof HomeActivity))
            throw new IllegalArgumentException("Home fragment cannot be attached to non-home activity");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            mUser = (User) savedInstanceState.getSerializable("USER");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("USER",mUser);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void onPause() {
        super.onPause();
        log.finest(getClass().getSimpleName()+" paused");
    }

    @Override
    public void onResume() {
        //if (mContent!=null) getContentView().setAlpha(0f);
        log.finest(getClass().getSimpleName()+" resumed");
        super.onResume();
        getContentView().requestApplyInsets();
        /*Fade fade = new Fade(Fade.IN);
        fade.addTarget(mContent);
        TransitionManager.beginDelayedTransition( ((HomeActivity)getActivity())
                .getRootViewGroup(),fade);*/
    }

    public void onShow(){
        getContentView().requestLayout();
        getContentView().requestApplyInsets();
        log.info(getClass()+" shown");
    }

    public void onHide(){

    }

    public HomeActivity getHomeActivity(){
        return (HomeActivity) getActivity();
    }

    public void hideHomeUI(boolean animated){
        getHomeActivity().hideHomeUI(animated);
    }

    public void showHomeUI(boolean animated){
        getHomeActivity().showHomeUI(animated);
    }
}
