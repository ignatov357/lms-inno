package com.awesprojects.innolib.fragments.home.abstracts;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.managers.UserManager;

/**
 * Created by ilya on 2/26/18.
 */

public class AbstractProfileFragment extends AbstractHomeFragment {

    private ImageView mProfileImageView;
    private TextView mUserTypeTextView;
    private TextView mFullNameTextView;

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
        setUserName(getUser().getName());
       // getFullNameTextView().setText(getUser().getName());
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
        String str;
        if (type==2){
            str = getResources().getString(R.string.user_type_librarian);
        }else if (type==1){
            str = getResources().getString(R.string.user_type_faculty).replace(' ','\n');
        }else{
            str = getResources().getString(R.string.user_type_student);
        }
        getUserTypeTextView().setText(str.toUpperCase());
    }
}
