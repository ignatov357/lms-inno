package com.awesprojects.innolib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.PatronProfileFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.innolib.widgets.OverdueIndicatorView;
import com.awesprojects.lmsclient.api.data.documents.Document;

import java.util.List;

/**
 * Created by Ilya on 3/26/2018.
 */

public class CheckedOutDocsAdapter extends RecyclerView.Adapter<CheckedOutDocsAdapter.CheckedOutDocsHolder>{

    public interface OnShowCheckedOutDocumentDetailsListener {
        void onShow(View holderRootView, Document document);
    }

    final PatronProfileFragment mPatronProfileFragment;
    List<Document> mDocuments;
    OnShowCheckedOutDocumentDetailsListener mOnShowCheckedOutDocumentDetailsListener;
    OnShowDetailsListener mHolderListener;

    public CheckedOutDocsAdapter(PatronProfileFragment profileFragment){
        mPatronProfileFragment = profileFragment;
        mHolderListener = new OnShowDetailsListener(this);
    }

    public void setOnShowCheckedOutDocumentDetailsListener(OnShowCheckedOutDocumentDetailsListener onShowCheckedOutDocumentDetailsListener) {
        mOnShowCheckedOutDocumentDetailsListener = onShowCheckedOutDocumentDetailsListener;
    }

    public void setDocuments(List<Document> documents){
        mDocuments = documents;
    }

    @Override
    public CheckedOutDocsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vg = ((LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.home_profile_checkedout_list_element,parent,false);
        return new CheckedOutDocsHolder(vg);
    }

    @Override
    public void onBindViewHolder(CheckedOutDocsHolder holder, int position) {
        Document d = mDocuments.get(position);
        holder.setItemOnClickListener(d,mHolderListener);
        holder.setDocType(d.getType());
        holder.setTitle(d.getTitle());
        holder.setAuthor(d.getAuthors());
        holder.setReturnDate(d.getCheckOutInfo()!=null ? d.getCheckOutInfo().getReturnTill() : -1);
    }

    @Override
    public int getItemCount() {
        return mDocuments==null ? 0 : mDocuments.size();
    }


    public static class CheckedOutDocsHolder extends RecyclerView.ViewHolder{

        TextView mDocName;
        TextView mDocType;
        TextView mDocAuthor;
        TextView mTimeLeft;
        View mOverdueIndicator;
        OverdueIndicatorView mOverdueIndicatorView;
        ViewGroup mRootView;

        public CheckedOutDocsHolder(View itemView) {
            super(itemView);
            mRootView = itemView.findViewById(R.id.home_profile_checkedout_element_root_view);
            mDocName = itemView.findViewById(R.id.home_profile_checkedout_element_title);
            mDocType = itemView.findViewById(R.id.home_profile_checkedout_element_type);
            mDocAuthor = itemView.findViewById(R.id.home_profile_checkedout_element_author);
            mTimeLeft = itemView.findViewById(R.id.home_profile_checkedout_element_left_time);
            mOverdueIndicator = itemView.findViewById(R.id.home_profile_checkedout_element_overdue_indicator);
            mOverdueIndicatorView = itemView.findViewById(R.id.home_profile_checkedout_element_overdue_indicator_imageview);
        }

        public void setTitle(String title){
            mDocName.setText(title);
        }

        public void setAuthor(String author){
            mDocAuthor.setText("By "+author);
        }

        public void setDocType(int type){
            String typeStr = DocumentManager.getInstance().getDocumentType(type,itemView.getContext());
            mDocType.setText(typeStr.toUpperCase());
        }

        public void setReturnDate(long millis){
            if (millis*1000<System.currentTimeMillis()){
                mOverdueIndicator.setVisibility(View.VISIBLE);
                mOverdueIndicatorView.setOverdue(true);
            }else{
                mOverdueIndicator.setVisibility(View.INVISIBLE);
                mOverdueIndicatorView.setOverdue(false);
            }
            String returnAt = DocumentManager.getPrettyReturnDate(millis).toLowerCase();
            mTimeLeft.setText(returnAt);
        }

        public void setItemOnClickListener(Document tag, View.OnClickListener listener) {
            mRootView.setOnClickListener(listener);
            mRootView.setTag(tag);
        }

    }

    public class OnShowDetailsListener implements View.OnClickListener {

        CheckedOutDocsAdapter mAdapter;

        public OnShowDetailsListener(CheckedOutDocsAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public void onClick(View view) {
            Document document = (Document) view.getTag();
            if (mAdapter.mOnShowCheckedOutDocumentDetailsListener!=null)
                mAdapter.mOnShowCheckedOutDocumentDetailsListener.onShow(view,document);
        }

    }

}
