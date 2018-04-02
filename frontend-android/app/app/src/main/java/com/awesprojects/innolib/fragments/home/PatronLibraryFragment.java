package com.awesprojects.innolib.fragments.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;

import com.awesprojects.innolib.R;
import com.awesprojects.innolib.adapters.PatronLibraryListAdapter;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractLibraryFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.data.documents.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by ilya on 2/26/18.
 */

public class PatronLibraryFragment extends AbstractLibraryFragment {

    public static final String TAG = "PatronLibraryFragment";
    public static Logger log = Logger.getLogger(TAG);

    PatronLibraryListAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.ItemAnimator mListItemAnimator;
    ArrayList<Document> mDocuments;
    OnDetailsShowListener mDetailsShowListener;
    TabLayout mTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabLayout = getContentView().findViewById(R.id.fragment_home_library_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mAdapter.setDocumentsType(tab.getPosition() - 1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mDocuments = new ArrayList<>();
        mAdapter = new PatronLibraryListAdapter(this);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mListItemAnimator = new DefaultItemAnimator();
        mDetailsShowListener = new OnDetailsShowListener(this);
        mAdapter.setOnShowDocumentDetailsListener(mDetailsShowListener);
        getList().setAdapter(mAdapter);
        getList().setLayoutManager(mLayoutManager);
        getList().setItemAnimator(mListItemAnimator);
        setRefreshListListener(() -> updateDocuments());
        if (savedInstanceState == null) {
            updateDocuments();
            log.fine("patron library creation done");
        } else {
            //noinspection unchecked
            mDocuments = (ArrayList<Document>) savedInstanceState.getSerializable("Documents");
            onDocumentsUpdated();
            log.fine("patron library recreation done");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void updateDocuments() {
        setListRefreshing(true);
        DocumentManager.getDocumentAsync(true, (documents) -> {
            if (documents instanceof ResponsableContainer) {
                //noinspection unchecked
                onDocumentsUpdated(((ResponsableContainer<Document[]>) documents).get());
            } else {
                //TODO: second case
            }
        });
    }

    public void updateArrayList(Document[] documents) {
        mDocuments.clear();
        Collections.addAll(mDocuments, documents);
    }

    public void onDocumentsUpdated(Document[] documents) {
        if (documents == null) return;
        updateArrayList(documents);
        onDocumentsUpdated();
    }

    public void onDocumentsUpdated() {
        mAdapter.setDocuments(mDocuments);
        mAdapter.notifyDataSetChanged();
        setListRefreshing(false);
        //log.info("documents received : "+documents.length);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("Documents", mDocuments);
        super.onSaveInstanceState(outState);
    }

    public int getDocumentPosition(Document document) {
        for (int i = 0; i < mDocuments.size(); i++) {
            if (mDocuments.get(i).getId() == document.getId())
                return i;
        }
        return -1;
    }

    public void onCheckOut(int documentId) {
        Document document = findDocumentById(documentId);

    }

    public Document findDocumentById(int documentId) {
        for (Document d : mDocuments)
            if (d.getId() == documentId) return d;
        return null;
    }

    public void onShowDocumentInfo(View documentHolder, Document document) {
        DocumentInfoFragment documentDetailFragment = new DocumentInfoFragment();
        documentDetailFragment.setOnCheckoutListener(((status, doc, reason) -> {
            doc.setInstockCount(doc.getInstockCount()-1);
            if (doc.getInstockCount()<=0) {
                int pos = getDocumentPosition(doc);
                mDocuments.remove(pos);
                mAdapter.setDocuments(mDocuments);
                mAdapter.notifyItemRemoved(pos);
                PatronProfileFragment.refreshCheckedoutList();
            }
        }));
        documentDetailFragment.setEnterTransition(new Slide(Gravity.BOTTOM));
        documentDetailFragment.setExitTransition(new Slide(Gravity.BOTTOM));
        Bundle args = new Bundle();
        args.putSerializable("DOCUMENT", document);
        documentDetailFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .addToBackStack("DetailsShow")
                .add(getHomeActivity().getHomeInterface().getOverlayContainer().getId(), documentDetailFragment, "DocumentInfoFragment")
                .commit();
    }


    public static class OnDetailsShowListener implements PatronLibraryListAdapter.OnShowDocumentDetailsListener {

        final PatronLibraryFragment mPatronLibraryFragment;

        public OnDetailsShowListener(PatronLibraryFragment patronLibraryFragment) {
            mPatronLibraryFragment = patronLibraryFragment;
        }

        @Override
        public void onShow(View viewHolderRoot, Document document) {
            mPatronLibraryFragment.onShowDocumentInfo(viewHolderRoot, document);
        }
    }

}
