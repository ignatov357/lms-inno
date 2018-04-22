package com.awesprojects.innolib.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awesprojects.innolib.R;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by ilya on 4/15/18.
 */

public class SearchCategoryMenuView extends LinearLayout{

    public interface SelectCallback {
        void onSearchCategoryItemSelected(SearchCategoryMenuView.Category category,MenuItem item);
    }

    public enum Category{
        DOCUMENT_TYPE,
        WHERE,
        AVAILABILITY
    }

    HashMap<Category,CategoryView> mCategories;
    LinearLayout mContentContainer;
    SelectCallback mSelectCallback;

    public SearchCategoryMenuView(Context context) {
        super(context);
        init();
    }

    public SearchCategoryMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchCategoryMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int mMenuHeight;

    public void init(){
        mCategories = new HashMap<>();
        mContentContainer = new LinearLayout(getContext());
        mContentContainer.setOrientation(HORIZONTAL);
        mMenuHeight = (int) getResources().getDimension(R.dimen.android_searchbar_height);
        addView(mContentContainer,-1,-1);
        CategoryView docType = new CategoryView(Category.DOCUMENT_TYPE,getContext());
        CategoryView whereToSearch = new CategoryView(Category.WHERE,getContext());
        CategoryView availabilityCategory = new CategoryView(Category.AVAILABILITY,getContext());
        docType.setOnClickListener(this::onClick);
        whereToSearch.setOnClickListener(this::onClick);
        availabilityCategory.setOnClickListener(this::onClick);
        docType.setSelectCallback(this::onMenuItemClicked);
        whereToSearch.setSelectCallback(this::onMenuItemClicked);
        availabilityCategory.setSelectCallback(this::onMenuItemClicked);
        mCategories.put(Category.DOCUMENT_TYPE,docType);
        mCategories.put(Category.WHERE,whereToSearch);
        mCategories.put(Category.AVAILABILITY,availabilityCategory);
        mContentContainer.addView(docType,new LayoutParams(-1,-2, 1f));
        mContentContainer.addView(whereToSearch,new LayoutParams(-1,-2,1f));
        mContentContainer.addView(availabilityCategory,new LayoutParams(-1,-2,1f));
    }

    public void setSelectCallback(SelectCallback callback){
        mSelectCallback = callback;
    }

    public void setCategoryDescription(Category category,String title,@DrawableRes int drawableId){
        CategoryView view = mCategories.get(category);
        view.setCategoryDescriptionText(title);
        view.setCategoryPreviewImage(drawableId);
    }

    public void setCategoryMenuRes(Category category, @MenuRes int menuRes){
        Menu menu = null;
        try{
            Class navMenu = Class.forName("android.support.design.internal.NavigationMenu");
            Constructor c = navMenu.getConstructor(Context.class);
            menu = (Menu) c.newInstance(getContext());
        }catch (Throwable t){}
        //mSearchMenu = new NavigationMenu(getContext());
        new MenuInflater(getContext()).inflate(menuRes,menu);
        setCategoryMenu(category,menu);
    }

    public void setCategoryMenu(Category category,Menu menu){
        CategoryView categoryView = mCategories.get(category);
        categoryView.setMenu(menu);
    }

    public void onClick(View view) {
        if (view instanceof CategoryView){
            CategoryView categoryView = ((CategoryView) view);
            categoryView.setExpanded(!categoryView.isExpanded(),categoryView.mCurrentMenuView,true);
        }
    }

    public void onMenuItemClicked(Category category,MenuItem item){
        if (mSelectCallback!=null)
            mSelectCallback.onSearchCategoryItemSelected(category,item);
    }


    private static class CategoryView extends FrameLayout{


        public interface SelectCallback{
            void onSelected(Category category,MenuItem item);
        }

        LinearLayout mContent;
        ImageView mCategoryPreview;
        TextView mCategoryDescription;
        Menu mMenu;
        MenuItem mCurrentItem;
        MenuItemView mCurrentMenuView;
        //MenuItemView mLastClickedView;
        LinearLayout mMenuContent;
        SelectCallback mSelectCallback;
        boolean mExpanded;
        final Category mCategory;
        int mMargin;
        int mMenuHeight;

        public CategoryView(Category category,@NonNull Context context) {
            super(context);
            mMenuHeight = (int) getResources().getDimension(R.dimen.android_searchbar_height);
            mMargin = (int) getContext().getResources().getDimension(R.dimen.android_searchmenu_margins);
            mCategory = category;
            mContent = new LinearLayout(context);
            mContent.setOrientation(HORIZONTAL);
            mContent.setGravity(Gravity.LEFT | Gravity.CENTER);
            mCategoryPreview = new ImageView(context);
            mCategoryDescription = new TextView(context);
            LinearLayout.LayoutParams cplp = new LinearLayout.LayoutParams(-2,-2);
            cplp.setMargins(0,mMargin,0,mMargin);
            mContent.addView(mCategoryPreview,cplp);
            LinearLayout.LayoutParams cdlp = new LinearLayout.LayoutParams(-2,-2);
            cdlp.setMargins(mMargin,mMargin,0,mMargin);
            mContent.addView(mCategoryDescription,cdlp);
            FrameLayout.LayoutParams cvlp = new FrameLayout.LayoutParams(-1,mMenuHeight);
            //cvlp.setMargins(mMargin,mMargin,mMargin,mMargin);
            addView(mContent,cvlp);
            mExpanded = false;
        }

        public void setSelectCallback(SelectCallback callback){
            mSelectCallback = callback;
        }

        public Category getCategory(){
            return mCategory;
        }

        public boolean isExpanded() {
            return mExpanded;
        }

        public void setMenu(Menu menu){
            mMenu = menu;
            mMenuContent = new LinearLayout(getContext());
            mMenuContent.setOrientation(VERTICAL);
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                MenuItemView menuItemView = new MenuItemView(this,getContext(),item);
                LinearLayout.LayoutParams milp = new LinearLayout.LayoutParams(-2,-2);
                //milp.setMargins(mMargin,mMargin,mMargin,mMargin);
                mMenuContent.addView(menuItemView);
                if (i==0) {
                    mCurrentMenuView = menuItemView;
                    mContent.setTransitionName(mCurrentMenuView.getTransitionName());
                    mCategoryPreview.setTransitionName(mCurrentMenuView.getImageView().getTransitionName());
                    mCategoryDescription.setTransitionName(mCurrentMenuView.getTitleView().getTransitionName());
                }
            }
            FrameLayout.LayoutParams mclp = new FrameLayout.LayoutParams(1,0);
            mclp.gravity = Gravity.CENTER;
           // addView(mMenuContent,mclp);
        }

        public void setCategoryPreviewImage(Drawable drawable){
            mCategoryPreview.setImageDrawable(drawable);
        }

        public void setCategoryPreviewImage(@DrawableRes int res){
            mCategoryPreview.setImageResource(res);
        }

        public void setCategoryDescriptionText(String text){
            mCategoryDescription.setText(text);
        }

        protected void onMenuItemViewClicked(View view){
            MenuItemView menuItemView = (MenuItemView) view;
            MenuItem clicked = (MenuItem) menuItemView.getTag();
            mCurrentItem = clicked;
            mCurrentMenuView = menuItemView;
            setCategoryPreviewImage(menuItemView.getImageDrawable());
            setCategoryDescriptionText(menuItemView.getTitleText());
           // linkTransitions(menuItemView);
            setExpanded(false,menuItemView,true);
            if (mSelectCallback!=null){
                mSelectCallback.onSelected(getCategory(),clicked);
            }
        }

        /*private void linkTransitions(MenuItemView menuItemView){
            mCategoryPreview.setTransitionName(hashCode()+"preview");
            mCategoryDescription.setTransitionName(hashCode()+"desc");
            menuItemView.getImageView().setTransitionName(hashCode()+"preview");
            menuItemView.getTitleView().setTransitionName(hashCode()+"desc");
        }*/

        public void setExpanded(boolean isExpanded,MenuItemView menuItemView,boolean animated){
            if (mMenu==null) return;
            if (animated)
                TransitionManager.beginDelayedTransition((ViewGroup) getRootView(),new ChangeBounds());
            if (isExpanded){
                removeView(mContent);
                FrameLayout.LayoutParams mclp = new FrameLayout.LayoutParams(-2,-2);
                //mclp.setMargins(mMargin,mMargin,mMargin,mMargin);
                //mclp.gravity = Gravity.LEFT;
                addView(mMenuContent,0,mclp);
            }else{
                mContent.setTransitionName(menuItemView.getTransitionName());
                mCategoryPreview.setTransitionName(menuItemView.getImageView().getTransitionName());
                mCategoryDescription.setTransitionName(menuItemView.getTitleView().getTransitionName());
                addView(mContent);
                removeView(mMenuContent);
            }
            mExpanded = isExpanded;
            //mMenuContent.requestLayout();
        }

        private class MenuItemView extends LinearLayout {

            MenuItem mItem;
            ImageView mImageView;
            TextView mTitleView;
            LinearLayout.LayoutParams mImageLP;
            LinearLayout.LayoutParams mTitleLP;
            //int mMargin;

            public MenuItemView(CategoryView catView,Context context, MenuItem item) {
                super(context);
                setTag(item);
                setOnClickListener(catView::onMenuItemViewClicked);
                setOrientation(HORIZONTAL);
                setTransitionName(hashCode()+"menuview");
                if (Build.VERSION.SDK_INT>=23) {
                    setBackgroundColor(getResources().getColor(R.color.cardview_light_background, null));
                }else{
                    setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
                }
                //TODO: fix dimen
                mItem = item;
                mImageView = new ImageView(context);
                mTitleView = new TextView(context);
                mTitleView.setText(item.getTitle());
                mTitleView.setTransitionName(hashCode()+"description");
                mImageView.setImageDrawable(item.getIcon());
                mImageView.setTransitionName(hashCode()+"preview");
                mImageLP = new LinearLayout.LayoutParams(-2, -2);
                mTitleLP = new LinearLayout.LayoutParams(-1, -2);
                mTitleLP.weight = 1;
                mTitleLP.setMargins(mMargin, mMargin, 0, mMargin);
                mTitleLP.gravity = Gravity.CENTER;
                mImageLP.setMargins(0, mMargin, 0, mMargin);
                mImageLP.gravity = Gravity.CENTER;
                addView(mImageView, mImageLP);
                addView(mTitleView, mTitleLP);
            }

            public View getImageView(){
                return mImageView;
            }

            public View getTitleView(){
                return mTitleView;
            }

            public Drawable getImageDrawable(){
                return mImageView.getDrawable();
            }

            public String getTitleText(){
                return mTitleView.getText()+"";
            }

        }
    }



}
