package com.awesprojects.innolib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.PatronProfileFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.data.documents.Document;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ilya on 3/26/2018.
 */

public class CheckedOutDocsAdapter extends RecyclerView.Adapter<CheckedOutDocsAdapter.CheckedOutDocsHolder>{

    final PatronProfileFragment mPatronProfileFragment;
    List<Document> mDocuments;

    public CheckedOutDocsAdapter(PatronProfileFragment profileFragment){
        mPatronProfileFragment = profileFragment;
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
            String typeStr = DocumentManager.getInstance().getDocumentType(type,itemView.getContext());
            mDocType.setText(typeStr.toUpperCase());
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

}
