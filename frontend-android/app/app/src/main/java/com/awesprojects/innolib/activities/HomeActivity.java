package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.transition.Fade;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.AbstractHomeFragment;
import com.awesprojects.innolib.fragments.LibrarianHomeFragment;
import com.awesprojects.innolib.fragments.PatronHomeFragment;
import com.awesprojects.lmsclient.api.data.users.User;

/**
 * Created by ilya on 2/4/18.
 */

public class HomeActivity extends Activity {

    protected User mCurrentUser;
    protected AbstractHomeFragment mHomeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mCurrentUser = (User) getIntent().getSerializableExtra("CURRENT_USER");
        if (savedInstanceState==null){
            if (mCurrentUser.getType()==2)
                mHomeFragment = new LibrarianHomeFragment();
            else
                mHomeFragment = new PatronHomeFragment();
            mHomeFragment.setUser(mCurrentUser);
            mHomeFragment.setEnterTransition(new Fade(Fade.IN));
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_home_main_container,mHomeFragment,"HomeFragment")
                    .commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
