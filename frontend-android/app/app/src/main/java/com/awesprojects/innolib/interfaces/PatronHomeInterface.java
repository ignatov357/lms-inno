package com.awesprojects.innolib.interfaces;

import android.app.Activity;

import com.awesprojects.innolib.R;

/**
 * Created by ilya on 2/4/18.
 */

public class PatronHomeInterface implements IHomeInterface {

    final Activity activity;

    public PatronHomeInterface(Activity activity){
        super();
        this.activity = activity;
    }


    @Override
    public void create() {
        activity.setContentView(R.layout.fragment_home_patron);
    }

    @Override
    public void destroy() {

    }
}
