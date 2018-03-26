package com.awesprojects.innolib.widgets;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;

/**
 * Created by Ilya on 3/23/2018.
 */

public class HomeBottomNavigationView extends BottomNavigationView {

    public HomeBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public HomeBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HomeBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        setFitsSystemWindows(true);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, 0, right, bottom);
    }
}
