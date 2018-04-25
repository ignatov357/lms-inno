package com.awesprojects.innolib.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesprojects.innolib.R;

import java.lang.reflect.Constructor;

/**
 * Created by ilya on 4/10/18.
 */

public class LibrarySearchBar extends LinearLayout {

    public interface SearchCallback{
        void onSearchStarted();
        void onSearchFinished();
        void onQueryTextSubmit(String text);
        void onQueryTextChange(String text);
    }

    public interface SearchMenuListener{
        void onSelected(MenuItem item);
    }

    SearchMenuListener mMenuCallback;
    SearchCallback mCallback;
    LinearLayout mContainerLayout;
    SquareImageViewButton mSearchImageView;
    SquareImageViewButton mMenuButton;
    EditText mInputEditText;
    Menu mSearchMenu;
    View mMenuView;
    RecyclerView mSearchList;
    float mSearchBarHeight;
    float mSearchBarElevation;
    boolean mSearching = false;
    boolean touchOnlyMode = false;
    boolean menuEnabled = false;
    boolean menuShown;

    public LibrarySearchBar(Context context) {
        super(context);
        init();
    }

    public LibrarySearchBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LibrarySearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setOrientation(VERTICAL);
        mSearchBarHeight = getResources().getDimension(R.dimen.android_searchbar_height);
        mSearchBarElevation = getResources().getDimension(R.dimen.cardview_default_elevation);
        mContainerLayout = new LinearLayout(getContext());
        mContainerLayout.setOrientation(HORIZONTAL);
        //TODO: move height to dimens
        addView(mContainerLayout, -1, (int) mSearchBarHeight);
        mInputEditText = new EditText(getContext());
        mInputEditText.setSingleLine(true);
        mInputEditText.setShowSoftInputOnFocus(true);
        mInputEditText.setBackgroundColor(Color.TRANSPARENT);
        mSearchImageView = new SquareImageViewButton(getContext());
        mSearchImageView.setImageResource(R.drawable.ic_search_black_24dp);
        mSearchImageView.setFocusable(true);
        //mSearchImageView.setFocusableInTouchMode(true);
        mSearchImageView.requestFocus();
        mMenuButton = new SquareImageViewButton(getContext());
        mMenuButton.setImageResource(R.drawable.ic_menu_black_24dp);
        mMenuButton.setVisibility(GONE);
        LayoutParams ilp = new LayoutParams(-1, -2, 1f);
        ilp.setMargins(16,8,16,8);
        mContainerLayout.addView(mInputEditText, ilp);
        mContainerLayout.addView(mSearchImageView,
                new LayoutParams(-2,-1));
        mContainerLayout.addView(mMenuButton,
                new LayoutParams(-2,-1));
        mSearchImageView.setOnClickListener((view) -> {
            if (touchOnlyMode){
                startSearch();
            }else {
                stopSearchAndSubmit();
            }
        });
        mInputEditText.setOnClickListener((view) -> {
            if (touchOnlyMode){
                startSearch();
            }else{
                if (!mSearching){
                    startSearch();
                    mInputEditText.requestFocus();

                }
            }
        });
        mInputEditText.setOnKeyListener(this::onInputTextKeyEvent);
        mMenuButton.setOnClickListener((view) -> {
            if (!menuShown) {
                showSearchMenu();
            }else{
                hideSearchMenu();
            }
        });
    }

    public void showKeyboard(){
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)
                getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    public boolean onInputTextKeyEvent(View view,int action,KeyEvent event){
        if (mSearching && !touchOnlyMode && event.getKeyCode()==KeyEvent.KEYCODE_ENTER){
            submitQuery();
            return true;
        }
        return false;
    }

    public void showSearchMenu(){
        if (mMenuView==null) return;
        TransitionManager.beginDelayedTransition((ViewGroup)getRootView(),new ChangeBounds());
        setElevation(4f);
        mMenuView.getLayoutParams().height = -2;
        mMenuView.requestLayout();
        menuShown = true;
    }

    public void hideSearchMenu(){
        if (mMenuView==null) return;
        TransitionManager.beginDelayedTransition((ViewGroup)getRootView(), new ChangeBounds());
        setElevation(0f);
        mMenuView.getLayoutParams().height = 0;
        mMenuView.requestLayout();
        menuShown = false;
    }

    public void setSearchButtonVisible(boolean visible){
        mSearchImageView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setTouchOnlyMode(boolean enabled){
        touchOnlyMode = enabled;
        //mInputEditText.setFocusableInTouchMode(!enabled);
        mInputEditText.setFocusable(!enabled);
        if (!enabled)
            mInputEditText.requestFocus();
    }

    public void setMenuEnabled(boolean enabled){
        menuEnabled = enabled;
        menuShown = false;
        mMenuButton.setVisibility( enabled ? VISIBLE : GONE );
    }

    public void setMenuView(View menuView){
        mMenuView = menuView;
    }

    /*@SuppressWarnings("unchecked")
    public void setMenu(@MenuRes int menuRes){
        try{
               Class navMenu = Class.forName("android.support.design.internal.NavigationMenu");
               Constructor c = navMenu.getConstructor(Context.class);
               mSearchMenu = (Menu) c.newInstance(getContext());
        }catch (Throwable t){}
        //mSearchMenu = new NavigationMenu(getContext());
        new MenuInflater(getContext()).inflate(menuRes,mSearchMenu);
        mSearchMenuView = new MenuView(getContext());
        mSearchMenuView.setMenu(mSearchMenu);
        addView(mSearchMenuView,-1,0);
    }

    public void setMenuListener(SearchMenuListener listener){
        mMenuCallback = listener;
    }*/

    public void setSearchCallbackListener(SearchCallback callback){
        mCallback = callback;
    }

    public void setSearchList(RecyclerView view){
        mSearchList = view;
    }
    public void startSearch(){
        if (mCallback!=null)
            mCallback.onSearchStarted();
        mSearching = true;
    }

    public void updateText(String newQuery){
        if (mCallback!=null)
            mCallback.onQueryTextChange(newQuery);
    }

    public void submitQuery(){
        String query = mInputEditText.getText().toString();
        if (mCallback!=null) {
            mCallback.onQueryTextSubmit(query);
        }
    }

    public void stopSearchAndSubmit(){
        String query = mInputEditText.getText().toString();
        if (mCallback!=null) {
            mCallback.onQueryTextSubmit(query);
            mCallback.onSearchFinished();
        }
        mSearching = false;
    }


    @SuppressLint("AppCompatCustomView")
    private static class SquareImageViewButton extends ImageButton {

        public SquareImageViewButton(Context context) {
            super(context);
            init();
        }

        public SquareImageViewButton(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public SquareImageViewButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init(){
            setScaleType(ScaleType.CENTER);
            setBackgroundColor(Color.TRANSPARENT);

        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }


}
