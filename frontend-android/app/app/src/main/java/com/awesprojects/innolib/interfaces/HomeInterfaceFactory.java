package com.awesprojects.innolib.interfaces;

import android.app.Activity;

/**
 * Created by ilya on 2/4/18.
 */

public class HomeInterfaceFactory {

    public static IHomeInterface createInterfaceByUserType(Activity activity, int type){
        if (type==2){
            return new LibrarianHomeInterface(activity);
        }
        if (type==1 || type==0){
            return new PatronHomeInterface(activity);
        }
        return null;
    }

}
