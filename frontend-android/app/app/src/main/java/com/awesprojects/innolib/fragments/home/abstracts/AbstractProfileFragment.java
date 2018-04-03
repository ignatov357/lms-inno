package com.awesprojects.innolib.fragments.home.abstracts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.StartActivity;
import com.awesprojects.innolib.fragments.home.SignOutConfirmFragment;
import com.awesprojects.innolib.managers.UserManager;

/**
 * Created by ilya on 2/26/18.
 */

public class AbstractProfileFragment extends AbstractHomeFragment {

    private ImageView mProfileImageView;
    private TextView mUserTypeTextView;
    private TextView mFullNameTextView;
    private View mSignOutButton;
    private LocalClickListener mClickListener;

    public ImageView getProfileImageView(){
        return mProfileImageView;
    }

    public TextView getFullNameTextView(){
        return mFullNameTextView;
    }

    public TextView getUserTypeTextView(){
        return mUserTypeTextView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.fragment_home_profile);
        mProfileImageView = getContentView().findViewById(R.id.fragment_home_profile_icon_imageview);
        mFullNameTextView = getContentView().findViewById(R.id.fragment_home_profile_full_name_textview);
        mUserTypeTextView = getContentView().findViewById(R.id.fragment_home_profile_user_type_textview);
        mSignOutButton = getContentView().findViewById(R.id.fragment_home_profile_signout_button);
        mClickListener = new LocalClickListener(this);
        mSignOutButton.setOnClickListener(mClickListener);
        setUserName(getUser().getName());
        setUserType(UserManager.getUserType(getUser()));
    }

    @Deprecated
    @Override
    public void setContentView(int contentId) {
        //undefined
    }

    public void setUserName(String fullName){
        getFullNameTextView().setText(fullName);
    }

    public void setUserType(int type){
        String str = UserManager.getUserTypeString(getActivity(),type);
        getUserTypeTextView().setText(str.toUpperCase());
    }

    private void onSignOutConfirm(boolean confirmed){
        if (!confirmed) return;
        boolean success = UserManager.getInstance().signOut(getActivity());
        if (success) {
            getActivity().startActivity(new Intent(getActivity(), StartActivity.class));
            getActivity().finish();
        }
    }

    private void signOut(){
        SignOutConfirmFragment signOutConfirmFragment = new SignOutConfirmFragment();
        signOutConfirmFragment.setCallbackListener(this::onSignOutConfirm);
        getFragmentManager().beginTransaction()
                .addToBackStack("SignOutConfirm")
                .add(getHomeActivity().getHomeInterface().getOverlayContainer().getId(), signOutConfirmFragment, "SignOutConfirmFragment")
                .commit();
    }

    protected static class LocalClickListener implements View.OnClickListener{

        private final AbstractProfileFragment mAbstractProfileFragment;

        public LocalClickListener(AbstractProfileFragment fragment){
            mAbstractProfileFragment = fragment;
        }
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.fragment_home_profile_signout_button:{
                    mAbstractProfileFragment.signOut();
                    break;
                }
            }
        }
    }
}
