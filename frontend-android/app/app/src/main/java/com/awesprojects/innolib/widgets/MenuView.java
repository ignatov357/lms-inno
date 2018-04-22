package com.awesprojects.innolib.widgets;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesprojects.innolib.R;

/**
 * Created by ilya on 4/15/18.
 */

public class MenuView extends LinearLayout implements View.OnClickListener {

    public interface MenuCallback{
        void onClick(MenuItem item);
    }

    private MenuCallback mMenuCallback;

    public MenuView(Context context) {
        super(context);
        init();
    }

    public MenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

    }



    public void setMenuCallback(MenuCallback callback){
        mMenuCallback = callback;
    }

    public void setMenu(Menu menu) {
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);
        removeAllViews();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            MenuItemView itemView = new MenuItemView(getContext(), item);
            addView(itemView, -1, 100);
        }
    }

    @Override
    public void onClick(View view) {
        MenuItem menuItem = (MenuItem) view.getTag();
        if (mMenuCallback != null)
            mMenuCallback.onClick(menuItem);
    }

    private class MenuItemView extends LinearLayout {

        MenuItem mItem;
        ImageView mImageView;
        TextView mTitleView;
        LinearLayout.LayoutParams mImageLP;
        LinearLayout.LayoutParams mTitleLP;
        int mMargin;

        public MenuItemView(Context context, MenuItem item) {
            super(context);
            setTag(item);
            setOnClickListener(MenuView.this);
            setOrientation(HORIZONTAL);
            //TODO: fix dimen
            mMargin = (int) context.getResources().getDimension(R.dimen.android_searchbar_elevation_raised);
            mItem = item;
            mImageView = new ImageView(context);
            mTitleView = new TextView(context);
            mTitleView.setText(item.getTitle());
            mImageView.setImageDrawable(item.getIcon());
            mImageLP = new LinearLayout.LayoutParams(-2, -2);
            mTitleLP = new LinearLayout.LayoutParams(-1, -2);
            mTitleLP.weight = 1;
            mTitleLP.setMargins(mMargin, mMargin, mMargin, mMargin);
            mImageLP.setMargins(mMargin, mMargin, mMargin, mMargin);
            addView(mImageView, mImageLP);
            addView(mTitleView, mTitleLP);
        }

    }

}
