package com.awesprojects.innolib.fragments;

import android.app.Fragment;

import com.awesprojects.lmsclient.api.data.users.User;

/**
 * Created by ilya on 2/25/18.
 */

public class AbstractHomeFragment extends Fragment {

    private User mUser;

    public void setUser(User user){
        mUser = user;
    }

    public User getUser() {
        return mUser;
    }
}
