package com.awesprojects.innolib.fragments.home;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.AutoTransition;
import android.transition.ChangeTransform;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.awesprojects.innolib.InnolibApplication;
import com.awesprojects.innolib.R;
import com.awesprojects.innolib.activities.HomeActivity;
import com.awesprojects.innolib.adapters.SearchListAdapter;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeFragment;
import com.awesprojects.innolib.fragments.home.abstracts.AbstractHomeOverlayFragment;
import com.awesprojects.innolib.managers.DocumentManager;
import com.awesprojects.innolib.widgets.LibrarySearchBar;
import com.awesprojects.innolib.widgets.SearchCategoryMenuView;
import com.awesprojects.lmsclient.api.ResponsableContainer;
import com.awesprojects.lmsclient.api.Search;
import com.awesprojects.lmsclient.api.data.documents.Document;
import com.awesprojects.lmsclient.api.internal.Responsable;

import java.util.ArrayList;

/**
 * Created by ilya on 4/10/18.
 */

public class SearchFragment extends AbstractHomeOverlayFragment
        implements LibrarySearchBar.SearchCallback,HomeActivity.KeyDispatchListener {

    LibrarySearchBar mSearchBar;
    SearchCategoryMenuView mCategoriesMenuView;
    RecyclerView mSearchListView;
    RecyclerView.LayoutManager mSearchListManager;
    SearchListAdapter mSearchListAdapter;
    RecyclerView.ItemAnimator mItemAnimator;
    ArrayList<Document> mFoundDocuments;
    ViewGroup mSearchElementsContainer;
    SearchParameters mSearchParameters;
    boolean isFinishing = false;

    public SearchFragment() {
        super();
        mFoundDocuments = new ArrayList<>();
        setEnterTransition(null);
        setExitTransition(null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_search);
        getHomeActivity().addKeyDispatchListener(this);
        mSearchBar = getContentView().findViewById(R.id.fragment_home_search_searchbar);
        mSearchBar.setTouchOnlyMode(false);
        mSearchBar.setSearchCallbackListener(this);
        mSearchBar.setMenuEnabled(false);
        mSearchBar.setSearchButtonVisible(true);
        mSearchElementsContainer = getContentView().findViewById(R.id.fragment_home_search_searchelements_container);
        mCategoriesMenuView = new SearchCategoryMenuView(getActivity());
        mCategoriesMenuView.setSelectCallback(this::onSearchCategoryItemSelected);
        mSearchBar.setMenuView(mCategoriesMenuView);
        mSearchListView = getContentView().findViewById(R.id.fragment_home_search_list_recyclerview);
        mSearchListManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mSearchListAdapter = new SearchListAdapter(this);
        mSearchListAdapter.setClickCallback(this::onSearchResultClicked);
        mItemAnimator = new DefaultItemAnimator();
        mSearchListView.setAdapter(mSearchListAdapter);
        mSearchListView.setItemAnimator(mItemAnimator);
        mSearchListView.setLayoutManager(mSearchListManager);
        mSearchListView.setHasFixedSize(true);
        mSearchListManager.setItemPrefetchEnabled(true);
        mSearchListView.setItemViewCacheSize(3);
        mSearchListAdapter.setDocuments(mFoundDocuments);
        mSearchParameters = new SearchParameters();
        initCategories();
        startupAnimation();
        mSearchBar.showKeyboard();
        mSearchBar.startSearch();
    }

    public boolean onKeyEvent(KeyEvent keyEvent) {
        if (isFinishing) return true;
        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            isFinishing = true;
            startFinishing();
            return true;
        }
        return false;
    }

    private void initCategories() {
        mCategoriesMenuView.setCategoryDescription(SearchCategoryMenuView.Category.DOCUMENT_TYPE,
                "Any", R.drawable.ic_storage_black_36dp);
        mCategoriesMenuView.setCategoryMenuRes(SearchCategoryMenuView.Category.DOCUMENT_TYPE,
                R.menu.home_search_doctype_menu);
        mCategoriesMenuView.setCategoryDescription(SearchCategoryMenuView.Category.WHERE,
                "Title", R.drawable.ic_description_black_36dp);
        mCategoriesMenuView.setCategoryMenuRes(SearchCategoryMenuView.Category.WHERE,
                R.menu.home_search_where_menu);
        mCategoriesMenuView.setCategoryDescription(SearchCategoryMenuView.Category.AVAILABILITY,
                "Any", R.drawable.ic_hourglass_empty_black_36dp);
        mCategoriesMenuView.setCategoryMenuRes(SearchCategoryMenuView.Category.AVAILABILITY,
                R.menu.home_search_availability_menu);
    }

    private void startupAnimation() {
         /*int colorFrom = Color.TRANSPARENT;
            int colorTo = 0;
            if (Build.VERSION.SDK_INT>=23)
                colorTo = getResources().getColor(R.color.colorPrimary,null);
            else
                colorTo = getResources().getColor(R.color.colorPrimary);*/
        View backgroundView = getContentView().findViewById(R.id.fragment_home_search_background_view);
        ValueAnimator va = ValueAnimator.ofFloat(0, 1);
        va.addUpdateListener(
                (evaluator) -> backgroundView.setAlpha((float) evaluator.getAnimatedValue())
        );
        va.setDuration(250);
        va.start();

        Handler h = new Handler();
        h.postDelayed(() -> {
            TransitionManager.beginDelayedTransition((ViewGroup) getContentView().getRootView());
            mSearchElementsContainer.addView(mCategoriesMenuView, -1, -2);
        }, 100);

    }

    public void startFinishing() {
        if (Build.VERSION.SDK_INT >= 23)
            TransitionManager.endTransitions((ViewGroup) getContentView());
        View cv = getContentView();
        Rect rect = new Rect(0, 0, cv.getWidth(), cv.getHeight());
        ValueAnimator va = ValueAnimator.ofFloat(1, 0);
        va.addUpdateListener(
                (evaluator) -> {
                    float state = (float) evaluator.getAnimatedValue();
                    rect.bottom = (int) (cv.getHeight() * state);
                    getContentView().setClipBounds(rect);
                }
        );
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}
            @Override
            public void onAnimationEnd(Animator animator) {
                onDestroy();
                getHomeActivity().getFragmentManager().beginTransaction()
                        .remove(SearchFragment.this)
                        .commit();
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
        va.setDuration(350);
        va.start();
        isFinishing = true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        getContentView().requestApplyInsets();
    }

    @Override
    public void onDestroy() {
        getHomeActivity().removeKeyDispatchListener(this);
        super.onDestroy();
    }

    public void onSearchResultClicked(View documentHolder, Document document){
        DocumentInfoFragment documentDetailFragment = new DocumentInfoFragment();
        documentDetailFragment.setOnFragmentClosedListener(() -> {
            getHomeActivity().addKeyDispatchListener(SearchFragment.this);
            mSearchBar.showKeyboard();
        });
        documentDetailFragment.setEnterTransition(new Slide(Gravity.BOTTOM));
        documentDetailFragment.setExitTransition(new Slide(Gravity.BOTTOM));
        Bundle args = new Bundle();
        args.putSerializable("DOCUMENT", document);
        documentDetailFragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .addToBackStack("DetailsShow")
                .add(getHomeActivity().getHomeInterface().getOverlayContainer().getId(),
                        documentDetailFragment, "DocumentInfoFragment")
                .commit();
        mSearchBar.hideKeyboard();
        getHomeActivity().removeKeyDispatchListener(this);
    }

    public void onSearchCategoryItemSelected(SearchCategoryMenuView.Category category, MenuItem item) {
        switch (category) {
            case DOCUMENT_TYPE: {
                mSearchParameters.mType = item.getTitle().toString();
                break;
            }
            case WHERE: {
                mSearchParameters.mWhere = item.getTitle().toString();
                break;
            }
            case AVAILABILITY: {
                mSearchParameters.availableOnly = item.getItemId() == R.id.available;
                break;
            }
        }
    }

    @Override
    public void onSearchStarted() {}

    @Override
    public void onSearchFinished() {
    }

    @Override
    public void onQueryTextSubmit(String text) {
        searchDocuments(text);
    }

    @Override
    public void onQueryTextChange(String text) {}

    //on new set of search documents
    public void updateListedElements(Document[] documents) {
        ArrayList<Document> newDocumentList = new ArrayList<>();
        for (Document doc : documents) {
            newDocumentList.add(doc);
        }
        ArrayList<Document> oldDocumentList = mFoundDocuments;
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldDocumentList.size();
            }
            @Override
            public int getNewListSize() {
                return newDocumentList.size();
            }
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return newDocumentList.get(newItemPosition).getId() ==
                        oldDocumentList.get(oldItemPosition).getId();
            }
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return newDocumentList.get(newItemPosition).getId() ==
                        oldDocumentList.get(oldItemPosition).getId();
            }
        });
        mFoundDocuments = newDocumentList;
        mSearchListAdapter.setDocuments(mFoundDocuments);
        result.dispatchUpdatesTo(mSearchListAdapter);
    }

    @SuppressWarnings("unchecked cast")
    public void onSearchResponseReceived(Responsable responsable) {
        //log.fine("search completed "+responsable.toString());
        if (responsable instanceof ResponsableContainer) {
            Document[] documents = ((ResponsableContainer<Document[]>) responsable).get();
            updateListedElements(documents);
            //Toast.makeText(getActivity(),"searched items : "+documents.length,0).show();
            //mSearchListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), responsable.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void searchDocuments(String query) {
        Search.Type type = getSearchType(mSearchParameters);
        Search.Where place = getSearchPlace(mSearchParameters);
        Search.Availability availability = getSearchAvailability(mSearchParameters);
        Toast.makeText(this.getActivity(), "parameters : " + mSearchParameters.toString(), Toast.LENGTH_SHORT).show();
        DocumentManager.getSearchDocumentsAsync(InnolibApplication.getAccessToken(), query, type, place, availability,
                this::onSearchResponseReceived);
    }

    public Search.Type getSearchType(SearchParameters parameters){
        Search.Type type = Search.Type.ANY;
        if (parameters.mType.equalsIgnoreCase("books")){
            type = Search.Type.BOOKS;
        }else if (parameters.mType.equalsIgnoreCase("articles")){
            type = Search.Type.ARTICLES;
        }else if (parameters.mType.equalsIgnoreCase("a/v")){
            type = Search.Type.AV;
        }
        return type;
    }

    public Search.Where getSearchPlace(SearchParameters parameters){
        Search.Where where = Search.Where.TITLE;
        if (parameters.mWhere.equalsIgnoreCase("author")){
            where = Search.Where.AUTHORS;
        }else if (parameters.mWhere.equalsIgnoreCase("keywords")){
            where = Search.Where.KEYWORDS;
        }
        return where;
    }

    public Search.Availability getSearchAvailability(SearchParameters parameters){
        Search.Availability availability = Search.Availability.ANY;
        if (parameters.availableOnly)
            availability = Search.Availability.AVAILABLE_ONLY;
        return availability;
    }

    private static class SearchParameters {
        public String mType = "all";
        public String mWhere = "title";
        public boolean availableOnly;

        @Override
        public String toString() {
            return "SearchParameters(type=" + mType + ", where=" + mWhere + ", availableOnly=" + availableOnly + ")";
        }
    }
}
