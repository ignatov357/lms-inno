package com.awesprojects.innolib.interfaces;

import android.app.Activity;
import android.os.Bundle;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.HomeActivity;

/**
 * Created by ilya on 2/4/18.
 */

public class LibrarianHomeInterface extends AbstractHomeInterface {

    final Activity activity;

    public LibrarianHomeInterface(HomeActivity activity){
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedState) {
        activity.setContentView(R.layout.interface_home_librarian);
    }

    @Override
    public void onDestroy() {

    }
}
