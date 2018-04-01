package com.awesprojects.innolib.fragments.home.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.LogActivity;

/**
 * Created by ilya on 2/26/18.
 */

public class AbstractSettingsFragment extends AbstractHomeFragment implements View.OnClickListener{

    Button mShowLogButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_settings);
        mShowLogButton = getContentView().findViewById(R.id.fragment_home_settings_show_log_button);
        mShowLogButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_home_settings_show_log_button: {showLog(); break;}
        }
    }

    public void showLog(){
        Intent intent = new Intent(getActivity(), LogActivity.class);
        getActivity().startActivity(intent);
    }
}
