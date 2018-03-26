package com.awesprojects.innolib.fragments.home.abstracts;

import android.os.Bundle;
import android.view.View;

/**
 * Created by ilya on 3/4/18.
 */

public class AbstractHomeOverlayFragment extends AbstractHomeFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getHomeActivity().getHomeInterface().getOverlayContainer().setFitsSystemWindows(true);
    }

    @Override
    public void setContentView(int contentId) {
        View view = getActivity().getLayoutInflater().inflate(contentId, null,false);
        assignContentView(view);
        //getContentView().setFitsSystemWindows(true);
        //getContentView().requestApplyInsets();
    }

    @Override
    public void onDestroy() {
        log.fine("overlay fragment on destroy");
        //getHomeActivity().getHomeInterface().getOverlayContainer().setFitsSystemWindows(false);
        super.onDestroy();
        //getHomeActivity().getHomeInterface().getOverlayContainer().requestApplyInsets();
    }

    @Override
    public void onResume() {
        super.onResume();
        getContentView().requestApplyInsets();
    }
}
