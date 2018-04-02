package com.awesprojects.innolib.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.utils.logger.LogStorageManager;

/**
 * Created by Ilya on 3/23/2018.
 */

public class LogActivity extends Activity implements View.OnClickListener,TabLayout.OnTabSelectedListener{

    TextView mLogTextView;
    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mTabLayout = findViewById(R.id.activity_log_tab_layout);
        mTabLayout.addOnTabSelectedListener(this);
        mLogTextView = findViewById(R.id.activity_log_main_textview);
        if (getIntent().hasExtra("LOG")){
            mLogTextView.setText(getIntent().getStringExtra("LOG"));
        }else{
            String log = LogStorageManager.getInstance().loadBaseLogFile();
            mLogTextView.setText(log);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {}

}
