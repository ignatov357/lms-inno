package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.awesprojects.innolib.R;

public class WelcomeActivity extends Activity implements View.OnClickListener{

    Button mFinishButton;
    SharedPreferences mPreferences;
    public static final String PREFERENCE_WELCOME_SCREEN_SHOWN = "welcome_screen_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mPreferences = getSharedPreferences("application_state",MODE_PRIVATE);
        mFinishButton = findViewById(R.id.activity_welcome_finish);
        mFinishButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == mFinishButton){
            mPreferences.edit().putBoolean(PREFERENCE_WELCOME_SCREEN_SHOWN,true).apply();
            finish();
        }
    }
}
