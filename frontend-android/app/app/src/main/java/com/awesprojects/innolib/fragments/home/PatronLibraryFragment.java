package com.awesprojects.innolib.fragments.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.DocumentsAPI;
import com.awesprojects.lmsclient.api.data.documents.Document;

/**
 * Created by ilya on 2/26/18.
 */

public class PatronLibraryFragment extends AbstractLibraryFragment {

    PatronLibraryListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Document[] mDocuments;
    OnCheckoutClickListener mOnCheckoutClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PatronLibraryListAdapter(this);
        mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mOnCheckoutClickListener = new OnCheckoutClickListener(this);
        getList().setAdapter(mAdapter);
        getList().setLayoutManager(mLayoutManager);
        setRefreshListListener(() -> updateDocuments());
        if (savedInstanceState==null) {
            updateDocuments();
            System.out.println("patron library creation done");
        }else{
            onDocumentsUpdated((Document[]) savedInstanceState.getSerializable("Documents"));
            System.out.println("patron library recreation done");
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void updateDocuments(){
        DocumentManager.getDocumentAsync((documents) -> onDocumentsUpdated(documents));
    }

    public void onDocumentsUpdated(Document[] documents){
        if (documents==null) return;
        mDocuments = documents;
        mAdapter.mDocuments = mDocuments;
        mAdapter.notifyDataSetChanged();
        setListRefreshing(false);
        System.out.println("documents received : "+documents.length);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("Documents",mDocuments);
        super.onSaveInstanceState(outState);
    }

    public void onCheckOut(int documentId){
        Document document = findDocumentById(documentId);
        LibraryCheckoutConfirmFragment confirmFragment = new LibraryCheckoutConfirmFragment();
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
        Button mCheckoutButton;
        int mStockColorNormal;

        public PatronLibraryListItemHolder(View itemView) {
            super(itemView);
            mCheckoutButton = itemView.findViewById(R.id.home_library_list_element_checkout_button);
            mTitleTextView = itemView.findViewById(R.id.home_library_list_element_title_textview);
            mAuthorsTextView = itemView.findViewById(R.id.home_library_list_element_authors_textview);
            mStockTextView = itemView.findViewById(R.id.home_library_list_element_stock_textview);
            mStockColorNormal = mStockTextView.getTextColors().getDefaultColor();
        }

        public void setTitle(String title){
            mTitleTextView.setText(title);
        }

        public void setAuthors(String authors){
            mAuthorsTextView.setText(authors);
        }

        public void setStockCount(int count){
            if (count==0){
                mStockTextView.setTextColor(Color.RED);
                mStockTextView.setText(R.string.home_library_list_document_stock_empty);
            }else if (count==1){
                mStockTextView.setTextColor(Color.YELLOW);
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

    }


    public static class PatronLibraryListAdapter extends RecyclerView.Adapter<PatronLibraryListItemHolder>{

        PatronLibraryFragment mPatronLibraryFragment;
        Document[] mDocuments;

        public PatronLibraryListAdapter(PatronLibraryFragment patronLibraryFragment){
            mPatronLibraryFragment = patronLibraryFragment;
            mDocuments = mPatronLibraryFragment.mDocuments;
        }

        @Override
        public PatronLibraryListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = mPatronLibraryFragment.getCompatLayoutInflater().inflate(R.layout.home_library_list_element,parent,false);
            return new PatronLibraryListItemHolder(v);
        }

        @Override
        public void onBindViewHolder(PatronLibraryListItemHolder holder, int position) {
            if (mDocuments==null) return;
            Document doc = mDocuments[position];
            holder.setCheckoutListener(doc.getId(),
                    mPatronLibraryFragment.mOnCheckoutClickListener);
            holder.setTitle(doc.getTitle());
            holder.setAuthors(doc.getAuthors());
            holder.setStockCount(doc.getInstockCount());
        }

        @Override
        public int getItemCount() {
            if (mPatronLibraryFragment.mDocuments!=null)
                return mPatronLibraryFragment.mDocuments.length;
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
