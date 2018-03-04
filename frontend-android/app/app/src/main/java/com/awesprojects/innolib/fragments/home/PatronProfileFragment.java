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

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.data.documents.Document;

import java.util.Calendar;

/**
 * Created by ilya on 2/4/18.
 */

public class PatronProfileFragment extends AbstractProfileFragment{

    RecyclerView mCheckedOutDocsView;
    CheckedOutDocsAdapter mCheckedOutDocsAdapter;
    SwipeRefreshLayout mCheckedOutSwipeRefreshLayout;
    NestedScrollView mNestedScrollView;
    Document[] mCheckedOutDocuments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNestedScrollView = getContentView().findViewById(R.id.fragment_home_profile_nested_scroll_view);
        mCheckedOutSwipeRefreshLayout = getContentView().findViewById(R.id.fragment_home_profile_list_swiperefresh);
        mCheckedOutDocsView = getContentView().findViewById(R.id.fragment_home_profile_checkedout_recyclerview);
        mCheckedOutDocsAdapter = new CheckedOutDocsAdapter(this);
        mCheckedOutDocsView.setAdapter(mCheckedOutDocsAdapter);
        mCheckedOutDocsView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mCheckedOutSwipeRefreshLayout.setOnRefreshListener(() -> updateCheckedOutDocs());
        if (savedInstanceState==null){
            updateCheckedOutDocs();
        }else{
            onUpdatedCheckedOutDocs((Document[])savedInstanceState.getSerializable("CHECKED_OUT_DOCUMENTS"));
        }
       // mNestedScrollView.addView(mCheckedOutDocsView);
    }

    public void updateCheckedOutDocs(){
        DocumentManager.getCheckedOutDocuments(InnolibApplication.getAccessToken(), (documents) -> {
            onUpdatedCheckedOutDocs(documents);
        });
    }

    public void onUpdatedCheckedOutDocs(Document[] documents){
        mCheckedOutDocuments = documents;
        mCheckedOutDocsAdapter.mDocuments = mCheckedOutDocuments;
        mCheckedOutDocsAdapter.notifyDataSetChanged();
        mCheckedOutSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("CHECKED_OUT_DOCUMENTS",mCheckedOutDocuments);
        super.onSaveInstanceState(outState);
    }

    public static class CheckedOutDocsHolder extends RecyclerView.ViewHolder{

        TextView mDocName;
        TextView mDocType;
        TextView mDocAuthor;
        TextView mTimeLeft;

        public CheckedOutDocsHolder(View itemView) {
            super(itemView);
            mDocName = itemView.findViewById(R.id.home_profile_checkedout_element_title);
            mDocType = itemView.findViewById(R.id.home_profile_checkedout_element_type);
            mDocAuthor = itemView.findViewById(R.id.home_profile_checkedout_element_author);
            mTimeLeft = itemView.findViewById(R.id.home_profile_checkedout_element_left_time);
        }

        public void setTitle(String title){
            mDocName.setText(title);
        }

        public void setAuthor(String author){
            mDocAuthor.setText("By "+author);
        }

        public void setDocType(int type){
            if (type==0){
                mDocType.setText("BOOK");
            }else if(type==1){
                mDocType.setText("ARTICLE");
            }else{
                mDocType.setText("A/V");
            }
        }

        public void setReturnDate(long millis){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(millis*1000);
            StringBuilder sb = new StringBuilder();
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH)+1;
            int year = c.get(Calendar.YEAR) % 100;
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            sb.append("return till ");
            sb.append(day).append(".").append(month<10 ? "0"+month : month).append(".").append(year < 10 ? "0"+year : year);
            sb.append(" at ").append(hour).append(":").append(minute<10 ? "0"+minute : minute);
            mTimeLeft.setText(sb.toString());
        }
    }


    public static class CheckedOutDocsAdapter extends RecyclerView.Adapter<CheckedOutDocsHolder>{

        final PatronProfileFragment mPatronProfileFragment;
        Document[] mDocuments;

        public CheckedOutDocsAdapter(PatronProfileFragment profileFragment){
            mPatronProfileFragment = profileFragment;
        }

        @Override
        public CheckedOutDocsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View vg = ((LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.home_profile_checkedout_list_element,parent,false);
            return new CheckedOutDocsHolder(vg);
        }

        @Override
        public void onBindViewHolder(CheckedOutDocsHolder holder, int position) {
            Document d = mDocuments[position];
            holder.setDocType(d.getType());
            holder.setTitle(d.getTitle());
            holder.setAuthor(d.getAuthors());
            holder.setReturnDate(d.getCheckOutInfo()!=null ? d.getCheckOutInfo().getReturnTill() : -1);
        }

        @Override
        public int getItemCount() {
            return mDocuments==null ? 0 : mDocuments.length;
        }
    }

}
