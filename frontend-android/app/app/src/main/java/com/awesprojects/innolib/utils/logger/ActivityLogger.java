package com.awesprojects.innolib.utils.logger;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.awesprojects.innolib.R;

import java.io.Serializable;

/**
 * Created by ilya on 3/5/18.
 */

public class ActivityLogger implements Serializable {

    public static ActivityLogger create(Activity activity){
        return new ActivityLogger().attach(activity);
    }


    protected transient Activity activity;

    protected transient LogSystem.LineReceiver receiver;

    public ActivityLogger(){

    }

    public ActivityLogger attach(Activity activity){
        this.activity = activity;
        receiver = (line) -> {
                if (mTextView!=null)
                    activity.runOnUiThread(() -> {
                        mTextView.append(line);
                        mScrollView.fullScroll(View.FOCUS_DOWN);
                    });
                mSavedLog += line;
            };
        LogSystem.attachLineReceiver(receiver);
        return this;
    }

    private transient ViewGroup mRootView;
    private transient ViewGroup mContentView;
    private transient TextView mTextView;
    private transient ScrollView mScrollView;
    private transient View mShowHideButton;
    protected String mSavedLog;
    boolean isShown;

    public void onCreate(Bundle savedInstanceState){
        mRootView = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.logger_layout,null,false);
        ((ViewGroup)activity.findViewById(android.R.id.content)).addView(mRootView);
        mContentView = mRootView.findViewById(R.id.logger_content_view);
        mScrollView = mRootView.findViewById(R.id.logger_main_scroll_view);
        mTextView = mRootView.findViewById(R.id.logger_layout_main_textview);
        mShowHideButton = mRootView.findViewById(R.id.logger_hideshow_button);
        mShowHideButton.setOnClickListener((view) -> showhide());
        isShown = false;
        if (mSavedLog!=null)
            mTextView.setText(mSavedLog);
    }

    public void onDestroy(){
        LogSystem.detachLineReceiver(receiver);
        mSavedLog = mTextView.getText().toString();
    }

    public void showhide(){
        if (isShown){
            hide();
        }else{
            show();
        }
    }

    public void show(){
        Slide slide = new Slide(Gravity.LEFT);
        TransitionManager.beginDelayedTransition(mRootView,slide);
        mContentView.setTranslationX(0f);
        mContentView.setVisibility(View.VISIBLE);
        isShown = true;
    }

    public void hide(){
        Slide slide = new Slide(Gravity.LEFT);
        TransitionManager.beginDelayedTransition(mRootView,slide);
        mContentView.setTranslationX(-activity.getResources().getDisplayMetrics().widthPixels);
       // mContentView.setAlpha(1f);
        isShown = false;
    }



}
