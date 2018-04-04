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
    String mBaseLog;
    String mDebugLog;
    boolean mTabRefreshed = false;
    int mDefaultLoadSize = 2345;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        mTabLayout = findViewById(R.id.activity_log_tab_layout);
        mTabLayout.addOnTabSelectedListener(this);
        mLogTextView = findViewById(R.id.activity_log_main_textview);
        if (getIntent().hasExtra("LOG")){
            mLogTextView.setText(getIntent().getStringExtra("LOG"));
            mTabLayout.setVisibility(View.GONE);
        }else{
            if (getIntent().hasExtra("LOG_TAB")){
                String tab = getIntent().getStringExtra("LOG_TAB");
                if (tab.equals("BASE")) {
                    mTabLayout.getTabAt(0).select();
                }else{
                    mTabLayout.getTabAt(1).select();
                }
            }else {
                mBaseLog = LogStorageManager.getInstance().loadBaseLogFile(mDefaultLoadSize);
                mDebugLog = LogStorageManager.getInstance().loadDebugLogFile(mDefaultLoadSize);
                mLogTextView.setText(mBaseLog);
            }
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
        if (tab.getPosition()==0){
            mBaseLog = LogStorageManager.getInstance().loadBaseLogFile(mDefaultLoadSize);
            mLogTextView.setText(mBaseLog);
        }else{
            mDebugLog = LogStorageManager.getInstance().loadDebugLogFile(mDefaultLoadSize);
            mLogTextView.setText(mDebugLog);
        }
        mTabRefreshed = false;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        if (mTabRefreshed) return;
        if (tab.getPosition()==0){
            mBaseLog = LogStorageManager.getInstance().loadBaseLogFile();
            mLogTextView.setText(mBaseLog);
        }else{
            mDebugLog = LogStorageManager.getInstance().loadDebugLogFile();
            mLogTextView.setText(mDebugLog);
        }
        mTabRefreshed = true;
    }

}
