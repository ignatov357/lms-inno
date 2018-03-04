package com.awesprojects.innolib.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.lmsclient.api.data.documents.Document;

/**
 * Created by ilya on 2/4/18.
 */

public class PatronProfileFragment extends AbstractProfileFragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView mCheckedOutDocsView;
    RecyclerView.Adapter<CheckedOutDocsHolder> mCheckedOutDocsAdapter;
    SwipeRefreshLayout mCheckedOutSwipeRefreshLayout;
    NestedScrollView mNestedScrollView;
    Document[] mCheckedOutDocuments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNestedScrollView = getContentView().findViewById(R.id.fragment_home_profile_nested_scroll_view);
        mCheckedOutSwipeRefreshLayout = getContentView().findViewById(R.id.fragment_home_profile_list_swiperefresh);
        mCheckedOutDocsView = getContentView().findViewById(R.id.fragment_home_profile_checkedout_recyclerview);
       // mCheckedOutDocsView.setNestedScrollingEnabled(true);
        mCheckedOutDocsAdapter = new CheckedOutDocsAdapter();
        mCheckedOutDocsView.setAdapter(mCheckedOutDocsAdapter);
        mCheckedOutDocsView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mCheckedOutSwipeRefreshLayout.setOnRefreshListener(this);
       // mNestedScrollView.addView(mCheckedOutDocsView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mCheckedOutSwipeRefreshLayout.setRefreshing(false);
    }


    public static class CheckedOutDocsHolder extends RecyclerView.ViewHolder{

        TextView mDocName;
        TextView mTimeLeft;

        public CheckedOutDocsHolder(View itemView) {
            super(itemView);
            mDocName = itemView.findViewById(R.id.home_profile_checkedout_element_title);
            mTimeLeft = itemView.findViewById(R.id.home_profile_checkedout_element_left_time);
        }
    }


    public static class CheckedOutDocsAdapter extends RecyclerView.Adapter<CheckedOutDocsHolder>{

        @Override
        public CheckedOutDocsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View vg = ((LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.home_profile_checkedout_list_element,parent,false);
            return new CheckedOutDocsHolder(vg);
        }

        @Override
        public void onBindViewHolder(CheckedOutDocsHolder holder, int position) {
            holder.mTimeLeft.setText((getItemCount()-position)+"");
            holder.mDocName.setText("Document "+position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

}
