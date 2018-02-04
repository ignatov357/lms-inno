package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.LoginFragment;

public class StartActivity extends Activity {

    FrameLayout mContainer;
    SharedPreferences mPreferences;
    Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("application_state",MODE_PRIVATE);
        setContentView(R.layout.activity_start);
        mContainer = findViewById(R.id.activity_start_main_container);
    }

    @Override
    public void onStart(){
        super.onStart();
        boolean isFirstStart = mPreferences.getBoolean("is_first_start",true);
        if (isFirstStart){
            Intent welcomeActivityIntent = new Intent(this,WelcomeActivity.class);
            startActivity(welcomeActivityIntent);
            mCurrentFragment = new LoginFragment();
            mCurrentFragment.setRetainInstance(false);
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_start_main_container,mCurrentFragment,"CurrentFragment")
                    .commit();
        }
    }
}
