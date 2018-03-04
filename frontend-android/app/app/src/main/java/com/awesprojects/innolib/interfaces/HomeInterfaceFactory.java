package com.awesprojects.innolib.interfaces;

import android.app.Activity;

import com.awesprojects.innolib.activities.HomeActivity;

/**
 * Created by ilya on 2/4/18.
 */

public class HomeInterfaceFactory {

    public static AbstractHomeInterface createInterfaceByUserType(HomeActivity activity, int type){
        if (true) return new PatronHomeInterface(activity);
        if (type==2){
            return new LibrarianHomeInterface(activity);
        }
        if (type==1 || type==0){
            return new PatronHomeInterface(activity);
        }
        return null;
    }

}
