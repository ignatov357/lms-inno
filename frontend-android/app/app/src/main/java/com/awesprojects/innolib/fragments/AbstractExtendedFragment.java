package com.awesprojects.innolib.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ilya on 3/1/18.
 */

public class AbstractExtendedFragment extends Fragment {

    private View mContent;

    public void setContentView(@LayoutRes int contentId){
        mContent = getActivity().getLayoutInflater().inflate(contentId,null);
    }

    public View getContentView(){
        return mContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    public LayoutInflater getCompatLayoutInflater(){
        if (Build.VERSION.SDK_INT>=26)
            return super.getLayoutInflater();
        else
            return getActivity().getLayoutInflater();
    }

}
