package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractLibraryFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.innolib.utils.logger.LogSystem;
import com.awesprojects.lmsclient.api.data.documents.Article;
import com.awesprojects.lmsclient.api.data.documents.Book;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.data.documents.EMaterial;

import java.util.ArrayList;

/**
 * Created by ilya on 2/26/18.
 */

public class PatronLibraryFragment extends AbstractLibraryFragment {

    PatronLibraryListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Document> mDocuments;
    OnCheckoutClickListener mOnCheckoutClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDocuments = new ArrayList<>();
        mAdapter = new PatronLibraryListAdapter(this);
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mOnCheckoutClickListener = new OnCheckoutClickListener(this);
        getList().setAdapter(mAdapter);
        getList().setLayoutManager(mLayoutManager);
        setRefreshListListener(() -> updateDocuments());
        if (savedInstanceState==null) {
            updateDocuments();
            LogSystem.ui.println("patron library creation done");
        }else{
            mDocuments = (ArrayList<Document>) savedInstanceState.getSerializable("Documents");
            onDocumentsUpdated();
            LogSystem.ui.println("patron library recreation done");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void updateDocuments(){
        DocumentManager.getDocumentAsync(true,(documents) -> onDocumentsUpdated(documents));
    }

    public void updateArrayList(Document[] documents){
        mDocuments.clear();
        for (int i = 0; i < documents.length; i++) {
            mDocuments.add(documents[i]);
        }
    }

    public void onDocumentsUpdated(Document[] documents) {
        if (documents == null) return;
        updateArrayList(documents);
        onDocumentsUpdated();
    }

    public void onDocumentsUpdated(){
        mAdapter.mDocuments = mDocuments;
        mAdapter.notifyDataSetChanged();
        setListRefreshing(false);
        //LogSystem.ui.println("documents received : "+documents.length);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("Documents",mDocuments);
        super.onSaveInstanceState(outState);
    }

    public int getDocumentPosition(Document document){
        for (int i = 0; i < mDocuments.size(); i++) {
            if (mDocuments.get(i).getId() == document.getId())
                return i;
        }
        return -1;
    }

    public void onCheckOut(int documentId){
        Document document = findDocumentById(documentId);
        LibraryCheckoutConfirmFragment confirmFragment = new LibraryCheckoutConfirmFragment();
        confirmFragment.setResultListener((code,doc,reason) -> {
            int pos = getDocumentPosition(doc);
            mDocuments.remove(pos);
            mAdapter.mDocuments = mDocuments;
            mAdapter.notifyItemRemoved(pos);
            PatronProfileFragment.refreshCheckedoutList();
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable("DOCUMENT",document);
        confirmFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .addToBackStack("CheckoutConfirm")
                .add(android.R.id.content,confirmFragment,"ConfirmFragment")
                .commit();
    }

    public Document findDocumentById(int documentId){
        for (Document d : mDocuments)
            if (d.getId()==documentId) return d;
        return null;
    }

    public static class PatronLibraryListItemHolder extends RecyclerView.ViewHolder{

        TextView mTitleTextView;
        TextView mAuthorsTextView;
        TextView mStockTextView;
        TextView mCheckoutUnavailableTextView;
        TextView mDocumentTypeTextView;
        TextView mInfoTopTextView;
        TextView mInfoBottomTextView;
        Button mCheckoutButton;
        View mBestsellerLayout;
        TextView mKeywordsTextView;
        int mStockColorNormal;
        int mColorRed;
        int mColorYellow;

        @SuppressWarnings("deprecated")
        public PatronLibraryListItemHolder(View itemView) {
            super(itemView);
            mColorYellow = itemView.getResources().getColor(R.color.colorDarkYellow);
            mColorRed = itemView.getResources().getColor(R.color.colorDarkRed);
            mKeywordsTextView = itemView.findViewById(R.id.home_library_list_element_keywords_textview);
            mBestsellerLayout = itemView.findViewById(R.id.home_library_list_element_bestseller_layout);
            mDocumentTypeTextView = itemView.findViewById(R.id.home_library_list_element_type_textview);
            mCheckoutButton = itemView.findViewById(R.id.home_library_list_element_checkout_button);
            mCheckoutUnavailableTextView = itemView.findViewById(R.id.home_library_list_element_checkout_unavailable_reason_textview);
            mTitleTextView = itemView.findViewById(R.id.home_library_list_element_title_textview);
            mAuthorsTextView = itemView.findViewById(R.id.home_library_list_element_authors_textview);
            mInfoTopTextView = itemView.findViewById(R.id.home_library_list_element_info_top_textview);
            mInfoBottomTextView = itemView.findViewById(R.id.home_library_list_element_info_bottom_textview);
            mStockTextView = itemView.findViewById(R.id.home_library_list_element_stock_textview);
            mStockColorNormal = mStockTextView.getTextColors().getDefaultColor();
        }

        public void setCheckoutAvailable(boolean available,String reason){
            mCheckoutButton.setVisibility(available ? View.VISIBLE : View.INVISIBLE);
            mCheckoutUnavailableTextView.setVisibility( available ? View.INVISIBLE : View.VISIBLE);
            if (!available){
                mCheckoutUnavailableTextView.setText(reason);
            }
        }

        public void setTopInfo(String topInfo){
            mInfoTopTextView.setVisibility( topInfo==null ? View.GONE : View.VISIBLE);
            if (topInfo!=null){
                mInfoTopTextView.setText(topInfo);
            }
        }

        public void setBottomInfo(String bottomInfo){
            mInfoBottomTextView.setVisibility( bottomInfo==null ? View.GONE : View.VISIBLE);
            if (bottomInfo!=null){
                mInfoBottomTextView.setText(bottomInfo);
            }
        }

        public void setDocumentType(int type){
            String typeStr = DocumentManager.getInstance().getDocumentType(type,itemView.getContext());
            mDocumentTypeTextView.setText(typeStr.toUpperCase());
        }

        public void setTitle(String title){
            mTitleTextView.setText(title);
        }

        public void setAuthors(String authors){
            mAuthorsTextView.setText("By "+authors);
        }

        public void setStockCount(int count){
            if (count==0){
                mStockTextView.setTextColor(mColorRed);
                mStockTextView.setText(R.string.home_library_list_document_stock_empty);
            }else if (count==1){
                mStockTextView.setTextColor(mColorYellow);
                mStockTextView.setText(R.string.home_library_list_document_stock_last);
            }else{
                String text = mStockTextView.getResources().getString(R.string.home_library_list_document_stock_default);
                mStockTextView.setTextColor(mStockColorNormal);
                mStockTextView.setText(count+" "+text);
            }
        }

        public void setCheckoutListener(int id,OnCheckoutClickListener onCheckoutClickListener){
            mCheckoutButton.setOnClickListener(onCheckoutClickListener);
            mCheckoutButton.setTag(id);
        }

        public void setBestseller(boolean is){
            if (is){
                mBestsellerLayout.setVisibility(View.VISIBLE);
            }else{
                mBestsellerLayout.setVisibility(View.GONE);
            }
        }

        public void setKeywords(String[] keywords){
            if (keywords==null || keywords.length == 0){
                mKeywordsTextView.setVisibility(View.GONE);
                mKeywordsTextView.setText("");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < keywords.length; i++) {
                sb.append("#").append(keywords[i]);
                if (i!=keywords.length-1)
                    sb.append(" ");
            }
            mKeywordsTextView.setVisibility(View.VISIBLE);
            mKeywordsTextView.setText(sb.toString());
        }

    }


    public static class PatronLibraryListAdapter extends RecyclerView.Adapter<PatronLibraryListItemHolder>{

        PatronLibraryFragment mPatronLibraryFragment;
        ArrayList<Document> mDocuments;
        String mCheckoutDisabledReference;
        String mCheckoutDisabledMagazine;

        public PatronLibraryListAdapter(PatronLibraryFragment patronLibraryFragment){
            mPatronLibraryFragment = patronLibraryFragment;
            mDocuments = mPatronLibraryFragment.mDocuments;
            mCheckoutDisabledReference = patronLibraryFragment.getString(R.string.home_library_list_document_checkout_unavailable_reference);
            mCheckoutDisabledMagazine = patronLibraryFragment.getString(R.string.home_library_list_document_checkout_unavailable_magazine);
        }

        @Override
        public PatronLibraryListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mPatronLibraryFragment.getCompatLayoutInflater().inflate(R.layout.home_library_list_element,parent,false);
            return new PatronLibraryListItemHolder(v);
        }

        @Override
        public void onBindViewHolder(PatronLibraryListItemHolder holder, int position) {
            if (mDocuments==null) return;
            Document doc = mDocuments.get(position);
            holder.setDocumentType(doc.getType());
            holder.setCheckoutListener(doc.getId(), mPatronLibraryFragment.mOnCheckoutClickListener);
            holder.setTitle(doc.getTitle());
            holder.setAuthors(doc.getAuthors());
            holder.setStockCount(doc.getInstockCount());
            holder.setKeywords(preparedKeywords(doc));
            holder.setCheckoutAvailable(true,null);
            if (doc instanceof Book) {
                holder.setBestseller(((Book) doc).isBestseller());
                if (((Book) doc).isReference())
                    holder.setCheckoutAvailable(false,mCheckoutDisabledReference.toUpperCase());
                holder.setTopInfo(((Book) doc).getEdition()+" edition");
                holder.setBottomInfo("Published by "+((Book) doc).getPublisher()+" in "+((Book) doc).getPublicationYear());
            }else{
                holder.setBestseller(false);
            }
            if (doc instanceof Article){
                holder.setTopInfo("Edited by "+((Article) doc).getJournalIssueEditors());
                holder.setBottomInfo("Published in "+((Article) doc).getJournalTitle()+" in "+((Article) doc).getJournalIssuePublicationDate());
            }
            if (doc instanceof EMaterial){
                holder.setTopInfo(null);
                holder.setBottomInfo(null);
            }
        }

        public String[] preparedKeywords(Document document){
            String keywords = document.getKeywords();
            if (keywords==null) return null;
            keywords = keywords.replace(" ","_").replace(",_",",");
            if (keywords.contains(","))
                return keywords.split(",");
            else
                return new String[]{keywords};
        }

        @Override
        public int getItemCount() {
            if (mPatronLibraryFragment.mDocuments!=null)
                return mPatronLibraryFragment.mDocuments.size();
            return 0;
        }
    }

    public static class OnCheckoutClickListener implements View.OnClickListener{

        final PatronLibraryFragment mPatronLibraryFragment;

        public OnCheckoutClickListener(PatronLibraryFragment patronLibraryFragment){
            mPatronLibraryFragment = patronLibraryFragment;
        }
        @Override
        public void onClick(View view) {
            mPatronLibraryFragment.onCheckOut((int)view.getTag());
        }
    }

}
