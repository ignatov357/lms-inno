package com.awesprojects.innolib.fragments.home.abstracts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.HomeActivity;
import com.awesprojects.innolib.fragments.AbstractExtendedFragment;
import com.awesprojects.innolib.utils.logger.LogSystem;
import com.awesprojects.lmsclient.api.data.users.User;

/**
 * Created by ilya on 2/25/18.
 */

public class AbstractHomeFragment extends AbstractExtendedFragment {

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
        LogSystem.ui.println(getClass()+" paused");
    }

    @Override
    public void onResume() {
        //if (mContent!=null) getContentView().setAlpha(0f);
        LogSystem.ui.println(getClass()+" resumed");
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
        LogSystem.ui.print(getClass()+" shown");
    }

    public void onHide(){

    }
}
