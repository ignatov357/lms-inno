package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.awesprojects.innolib.R;

public class WelcomeActivity extends Activity implements View.OnClickListener{

    Button mFinishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mFinishButton = findViewById(R.id.activity_welcome_finish);
    }


    @Override
    public void onClick(View view) {
        if (view == mFinishButton){
            finish();
        }
    }
}
