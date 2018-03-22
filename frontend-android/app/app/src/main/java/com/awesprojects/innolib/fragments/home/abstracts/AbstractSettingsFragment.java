package com.awesprojects.innolib.fragments.home.abstracts;

import android.os.Bundle;

import com.awesprojects.innolib.R;

/**
 * Created by ilya on 2/26/18.
 */

public class AbstractSettingsFragment extends AbstractHomeFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_settings);
    }
}
