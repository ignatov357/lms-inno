package com.awesprojects.innolib.interfaces;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.view.ViewGroup;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.HomeActivity;
import com.awesprojects.innolib.fragments.home.PatronLibraryFragment;
import com.awesprojects.innolib.fragments.home.PatronProfileFragment;
import com.awesprojects.innolib.fragments.home.PatronSettingsFragment;

/**
 * Created by ilya on 2/4/18.
 */

public class PatronHomeInterface extends AbstractHomeInterface {

    final HomeActivity activity;

    BottomNavigationView mBottomNavigationView;
    ViewGroup mMainFragmentContainer;
    ViewGroup mFragmentRootContainer;

    @Override
    public ViewGroup getOverlayContainer() {
        return mFragmentRootContainer;
    }

    public PatronHomeInterface(HomeActivity activity){
        super(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedState) {
        setContentView(R.layout.interface_home_patron);
        mFragmentRootContainer = activity.findViewById(R.id.fragment_home_root_container);
        mBottomNavigationView = activity.findViewById(R.id.fragment_home_patron_bottom_navigation_view);
        mMainFragmentContainer = activity.findViewById(R.id.fragment_home_patron_main_container);
        setBottomNavigationView(mBottomNavigationView);
        setHomeFragmentContainer(mMainFragmentContainer);
        if (savedState==null){
            createFragments();
            mBottomNavigationView.setSelectedItemId(R.id.home_patron_main_menu_library);
        }
    }

    @Override
    public void onDestroy() {

    }

    public void createFragments(){
        addHomeFragment(R.id.home_patron_main_menu_profile,PatronProfileFragment.class);
        addHomeFragment(R.id.home_patron_main_menu_library,PatronLibraryFragment.class);
        addHomeFragment(R.id.home_patron_main_menu_settings,PatronSettingsFragment.class);
    }


}
