package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeFragment;

/**
 * Created by Ilya on 3/20/2018.
 */

public class DocumentDetailFragment extends AbstractHomeFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideHomeUI(true);
        setContentView(R.layout.fragment_document_details);
    }

    @Override
    public void onDestroy() {
        showHomeUI(true);
        super.onDestroy();
    }

}
