package com.awesprojects.innolib.fragments.home.abstracts;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;

import com.awesprojects.innolib.R;

/**
 * Created by ilya on 2/26/18.
 */

public class AbstractLibraryFragment extends AbstractHomeFragment {

    private RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public RecyclerView getList(){
        return mRecyclerView;
    }

    public void setRefreshListListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener){
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void setListRefreshing(boolean is){
        if (mSwipeRefreshLayout!=null)
            mSwipeRefreshLayout.setRefreshing(is);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_library);
        mSwipeRefreshLayout = getContentView().findViewById(R.id.fragment_home_library_list_swiperefresh);
        mRecyclerView = getContentView().findViewById(R.id.fragment_home_library_main_recyclerview);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
