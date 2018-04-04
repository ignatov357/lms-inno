package com.awesprojects.innolib.interfaces;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.transition.Visibility;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.awesprojects.innolib.activities.HomeActivity;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeFragment;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by ilya on 2/4/18.
 */

public abstract class AbstractHomeInterface {

    public abstract ViewGroup getOverlayContainer();

    public static final String TAG = "HomeInterface";
    public static Logger log = Logger.getLogger(TAG);

    final HomeActivity mActivity;
    private BottomNavigationView mBottomNavigationView;
    private HashMap<Integer,Class<? extends AbstractHomeFragment>> mHomeFragmentClassMap;
    private HashMap<Integer,AbstractHomeFragment> mHomeFragmentMap;
    private HashMap<Integer,Bundle> mFragmentStates;
    private AbstractHomeFragment mCurrentFragment;
    private ViewGroup mHomeFragmentContainer;
    private ViewGroup mContentView;
    private int mSelectedItem = -1;

    public AbstractHomeInterface(HomeActivity activity){
        mActivity = activity;
        mHomeFragmentMap = new HashMap<>();
        mHomeFragmentClassMap = new HashMap<>();
        mFragmentStates = new HashMap<>();
    }

    public void showHomeUI(boolean animated){
        log.fine("home ui show");
        if (mBottomNavigationView!=null){
            TransitionManager.beginDelayedTransition(mContentView);
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(mBottomNavigationView);
            slide.setStartDelay(0);
            slide.setMode(Visibility.MODE_IN);
            mBottomNavigationView.setVisibility(View.VISIBLE);
            //mBottomNavigationView.setTranslationY(0);
        }
    }

    public void hideHomeUI(boolean animated){
        log.fine("home ui hide");
        if (mBottomNavigationView!=null){
            TransitionManager.beginDelayedTransition(mContentView);
            Slide slide = new Slide(Gravity.BOTTOM);
            slide.addTarget(mBottomNavigationView);
            slide.setMode(Visibility.MODE_OUT);
            slide.setStartDelay(0);
            //TransitionManager.go(new Scene(mContentView));
            mBottomNavigationView.setVisibility(View.GONE);
            //mBottomNavigationView.setTranslationY(mBottomNavigationView.getHeight());
        }
    }

    public final FragmentManager getFragmentManager(){
        if (mActivity==null)
            return null;
        return mActivity.getFragmentManager();
    }

    public final void setContentView(@LayoutRes int layoutId){
        ViewGroup vg = (ViewGroup) mActivity.getLayoutInflater().inflate(layoutId,null);
        mActivity.setContentView(vg);
        mContentView = vg;
    }

    public ViewGroup getContentView(){
        return mContentView;
    }

    public ViewGroup getRootViewGroup(){
        return getContentView();
    }

    public final void create(Bundle savedState){
        if (savedState!=null){
            loadFragmentsFromBundle(savedState);
            loadFragmentsStatesFromBundle(savedState);
            mSelectedItem = savedState.getInt("SelectedItem");

        }
        //onMenuItemIdSelected(mSelectedItem);
        onCreate(savedState);
    }

    public abstract void onCreate(Bundle savedState);


    public final void destroy(){

        onDestroy();
    }

    public abstract void onDestroy();

    public void addHomeFragment(int menuID,Class<? extends AbstractHomeFragment> fragment){
        mHomeFragmentClassMap.put(menuID,fragment);
        AbstractHomeFragment abstractHomeFragment;
        try {
            abstractHomeFragment = fragment.newInstance();
            addTransitions(abstractHomeFragment);
            mHomeFragmentMap.put(menuID,abstractHomeFragment);
            //getFragmentManager().beginTransaction().attach(abstractHomeFragment);
        } catch (Throwable ignored){}
    }

    public void setBottomNavigationView(BottomNavigationView bnv){
        mBottomNavigationView = bnv;
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavViewClickListener(this));
    }

    public void onMenuItemSelected(MenuItem menuItem){
        onMenuItemIdSelected(menuItem.getItemId());
    }
    public void onMenuItemIdSelected(int itemId){
        if (mSelectedItem==itemId) return;
        AbstractHomeFragment fragment = mHomeFragmentMap.get(itemId);
        if (fragment!=null){
            setFragment(itemId,fragment);
        }else{
            Class<? extends AbstractHomeFragment> fragClass = mHomeFragmentClassMap.get(itemId);
            if (fragClass!=null){
                try {
                    fragment = fragClass.newInstance();
                    addTransitions(fragment);
                    mHomeFragmentMap.put(itemId,fragment);
                    setFragment(itemId,fragment);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        mSelectedItem = itemId;
    }

    public static void addTransitions(AbstractHomeFragment abstractHomeFragment){
        Fade fadeIn = new Fade(Fade.IN);
        fadeIn.setDuration((long)(0.64f*fadeIn.getDuration()));
        Fade fadeOut = new Fade(Fade.OUT);
        fadeOut.setStartDelay(fadeIn.getDuration());
        Slide slideIn = new Slide(Gravity.BOTTOM);
        //slideIn.setDuration((long)(0.64f*slideIn.getDuration()));
        TransitionSet setIn = new TransitionSet();
        //setIn.setDuration((long)(setIn.getDuration()*0.64f));
        setIn.addTransition(fadeIn);
        setIn.addTransition(slideIn);
        abstractHomeFragment.setRetainInstance(false);
        //abstractHomeFragment.setReturnTransition(fadeIn);
        //abstractHomeFragment.setReenterTransition(setIn);
        abstractHomeFragment.setEnterTransition(setIn);
        abstractHomeFragment.setExitTransition(fadeOut);
    }

    public static void removeTransitions(AbstractHomeFragment abstractHomeFragment){
        abstractHomeFragment.setEnterTransition(null);
        //abstractHomeFragment.setExitTransition(null);
    }

    public void setHomeFragmentContainer(ViewGroup container){
        mHomeFragmentContainer = container;
        if (mSelectedItem!=-1) {
            mCurrentFragment=null;
            int sI = mSelectedItem;
            mSelectedItem++;
            onMenuItemIdSelected(sI);
        }
    }

    public final void resume(){
        if ((mCurrentFragment!=null) && (!mCurrentFragment.isAdded())){
            int tempId = mCurrentFragment.getMenuId();
            AbstractHomeFragment tempFrag = mCurrentFragment;
            mCurrentFragment=null;
            setFragment(tempId,tempFrag,false);
        }
    }


    public final void pause(){

    }

    private void setFragment(int id,AbstractHomeFragment fragment){
        setFragment(id, fragment,true);
    }

    private void setFragment(int id,AbstractHomeFragment fragment, boolean animated){
        mHomeFragmentMap.put(id,fragment);
        if (animated && fragment.getEnterTransition()==null)
            addTransitions(fragment);
        if (!animated && fragment.getEnterTransition()!=null)
            removeTransitions(fragment);
        fragment.setUser(mActivity.getUser());
        fragment.setRetainInstance(false);
        FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
        if (mCurrentFragment!=null) {
            saveFragmentState(Integer.parseInt(mCurrentFragment.getTag()), mCurrentFragment);
            ft.remove(mCurrentFragment);
        }
        restoreFragmentState(id,fragment);
        ft.add(mHomeFragmentContainer.getId(),fragment,id+"");
        ft.commit();
        mCurrentFragment = fragment;
        log.finest("current fragment : "+mCurrentFragment.getClass()+" hash:"+mCurrentFragment.hashCode());
    }

    public final void saveInstanceState(Bundle bundle){
        log.finer("save");
        if (mCurrentFragment!=null && mCurrentFragment.getTag()!=null)
            mCurrentFragment.setMenuId(Integer.parseInt(mCurrentFragment.getTag()));
        saveFragmentsToBundle(bundle);
        saveFragmentsStatesToBundle(bundle);
        bundle.putInt("SelectedItem",mSelectedItem);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Map.Entry<Integer,AbstractHomeFragment> f : mHomeFragmentMap.entrySet()){
            if (f.getValue().isAdded())
                ft.remove(f.getValue());
        }
        ft.commit();
        onSaveInstanceState(bundle);
    }

    public void onSaveInstanceState(Bundle bundle){

    }

    public final void restoreInstanceState(Bundle bundle){
        log.finer("restore");
        if (mSelectedItem!=-1)
            onMenuItemIdSelected(mSelectedItem);
        onRestoreInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle){

    }


    private void restoreFragmentState(int id,Fragment fragment){
        Bundle b = mFragmentStates.get(id);
        log.finest("restore fragment state "+fragment.getClass()+" "+(b!=null));
        if (b==null) return;
        try {
            Class frag = Class.forName("android.app.Fragment");
            Field state = frag.getDeclaredField("mSavedFragmentState");
            state.setAccessible(true);
            state.set(fragment,b);
            //fragment.setArguments(b);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }


    private void loadFragmentsFromBundle(Bundle savedState){
        //mHomeFragmentMap = (HashMap<Integer, AbstractHomeFragment>)
        //        savedState.getSerializable("FragmentsMap");
        //noinspection unchecked
        mHomeFragmentClassMap = (HashMap<Integer, Class<? extends AbstractHomeFragment>>)
                savedState.getSerializable("FragmentClassesHashMap");
       /* for (Map.Entry<Integer,Class<? extends AbstractHomeFragment>> fragmentPair
                : mHomeFragmentClassMap.entrySet()){
            AbstractHomeFragment fragment = (AbstractHomeFragment)
                    getFragmentManager().findFragmentByTag(fragmentPair.getKey()+"");
            if (fragment!=null){
                mHomeFragmentMap.put(fragmentPair.getKey(),fragment);
            }
        }*/
    }

    private void loadFragmentsStatesFromBundle(Bundle savedState){
        //noinspection unchecked
        mFragmentStates = (HashMap<Integer,Bundle>) savedState.getSerializable("FragmentsStates");
    }

    private void saveFragmentsToBundle(Bundle savingState){
        //savingState.putSerializable("CurrentFragment", mCurrentFragment.class);
        savingState.putSerializable("FragmentClassesHashMap",mHomeFragmentClassMap);
        //savingState.putSerializable("FragmentsMap",mHomeFragmentMap);
    }

    private void saveFragmentsStatesToBundle(Bundle savingState){
        savingState.putSerializable("FragmentsStates",mFragmentStates);
    }
    private void saveFragmentState(int id,AbstractHomeFragment fragment){
        Bundle b = mFragmentStates.get(id);
        if (b==null)
            b = new Bundle();
        fragment.onSaveInstanceState(b);
        log.finest("saving fragment state "+fragment.getClass()+" "+b.toString());
        mFragmentStates.put(id,b);
    }

    private static class BottomNavViewClickListener implements BottomNavigationView.OnNavigationItemSelectedListener{

        private final AbstractHomeInterface mAbstractHomeInterface;

        public BottomNavViewClickListener(AbstractHomeInterface abstractHomeInterface){
            mAbstractHomeInterface = abstractHomeInterface;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mAbstractHomeInterface.onMenuItemSelected(item);
            return true;
        }
    }

}
