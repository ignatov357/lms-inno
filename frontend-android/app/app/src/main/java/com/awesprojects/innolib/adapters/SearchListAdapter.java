package com.awesprojects.innolib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.fragments.home.SearchFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.data.documents.Document;

import java.util.ArrayList;

/**
 * Created by ilya on 4/13/18.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.SearchListItemHolder> {

    public interface ClickCallback {
        void onSearchResultClicked(View documentHolder, Document document);
    }

    private SearchFragment mSearchFragment;
    private LayoutInflater mLayoutInflater;
    private ArrayList<Document> mDocuments;
    private String mSearchQuery;
    private ClickCallback mClickCallback;

    public SearchListAdapter(SearchFragment fragment) {
        mLayoutInflater = fragment.getCompatLayoutInflater();
        mSearchQuery = "";
    }

    public void setClickCallback(ClickCallback clickCallback){
        mClickCallback = clickCallback;
    }

    public void setDocuments(ArrayList<Document> documents) {
        mDocuments = documents;
    }

    public void setSearchQuery(String query) {
        mSearchQuery = query;
    }

    @Override
    public SearchListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchView = mLayoutInflater.inflate(R.layout.home_search_list_element, parent, false);
        SearchListItemHolder holder = new SearchListItemHolder(searchView);
        holder.getItemView().setOnClickListener(this::onItemClicked);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchListItemHolder holder, int position) {
        if (mDocuments == null) return;
        holder.setDocument(mDocuments.get(position), mSearchQuery);
    }

    @Override
    public int getItemCount() {
        if (mDocuments == null) return 0;
        return mDocuments.size();
    }

    private void onItemClicked(View view){
        if (view.getTag()!=null){
            Document document = (Document) view.getTag();
            if (mClickCallback!=null){
                mClickCallback.onSearchResultClicked(view,document);
            }
        }
    }

    protected static class SearchListItemHolder extends RecyclerView.ViewHolder {

        private Document mDocument;
        private TextView mTitleTextView;
        private TextView mAuthorsTextView;
        private TextView mKeywordsTextView;
        private TextView mTypeTextView;
        private ImageView mPreviewImageView;

        public SearchListItemHolder(View itemView) {
            super(itemView);
            mPreviewImageView = itemView.findViewById(R.id.home_search_element_preview_imageview);
            mTypeTextView = itemView.findViewById(R.id.home_search_element_type_textview);
            mTitleTextView = itemView.findViewById(R.id.home_search_element_title_textview);
            mAuthorsTextView = itemView.findViewById(R.id.home_search_element_authors_textview);
            mKeywordsTextView = itemView.findViewById(R.id.home_search_element_keywords_textview);
        }

        public View getItemView(){
            return itemView;
        }

        public void setDocument(Document document, String findValue) {
            itemView.setTag(document);
            setType(document);
            setTitle(document.getTitle(), findValue);
            setAuthors(document.getAuthors(), findValue);
            setKeywords(document, findValue);
            mDocument = document;
        }

        private void setType(Document doc) {
            mTypeTextView.setText(DocumentManager.getInstance().getDocumentType(
                    doc.getType(), itemView.getContext()));
            mPreviewImageView.setImageResource(getImageResource(doc.getType()));
        }

        private int getImageResource(int type) {
            switch (type) {
                case 0:
                    return R.drawable.ic_library_books_black_36dp;
                case 1:
                    return R.drawable.ic_insert_drive_file_black_36dp;
                case 2:
                    return R.drawable.ic_video_library_black_36dp;
            }
            return 0;
        }

        private void setTitle(String title, String findValue) {
            mTitleTextView.setText(title);
        }

        private void setAuthors(String authors, String findValue) {
            mAuthorsTextView.setText(authors);
        }

        private void setKeywords(Document document, String findValue) {
            String[] keywords = DocumentManager.preparedKeywords(document);
            mKeywordsTextView.setText("");
            for (int i = 0; i < keywords.length; i++) {
                mKeywordsTextView.append("#");
                mKeywordsTextView.append(keywords[i]);
                mKeywordsTextView.append(" ");
            }
            //mKeywordsTextView.append();
        }

    }

}
