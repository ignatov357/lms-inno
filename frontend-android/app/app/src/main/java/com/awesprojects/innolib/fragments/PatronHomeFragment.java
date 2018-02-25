package com.awesprojects.innolib.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awesprojects.innolib.R;

/**
 * Created by ilya on 2/4/18.
 */

public class PatronHomeFragment extends AbstractHomeFragment {

    private View mContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_patron,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mContent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
