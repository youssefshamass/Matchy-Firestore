package com.se.matchy.ui.matches.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.gturedi.views.StatefulLayout;
import com.se.matchy.R;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.ui.BaseActivity;
import com.se.matchy.framework.ui.decorators.VerticalSpaceDecorator;
import com.se.matchy.ui.matches.adapters.CriteriaAdapter;
import com.se.matchy.ui.matches.adapters.MatchAdapter;
import com.se.matchy.ui.matches.viewmodel.MatchesViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchesActivity extends BaseActivity implements Observer<Response> {

    //region Constants

    private static final String EXTRA_CHAPTER_ID = MatchesActivity.class.getSimpleName() + ".CHAPTER_ID";
    private static final String EXTRA_OUTCOME = MatchesActivity.class.getSimpleName() + ".OUTCOME";

    //endregion

    //region Variables

    private MatchesViewModel mMatchesViewModel;

    @BindView(R.id.activity_matches_header_text_view)
    public TextView mHeaderTextView;
    @BindView(R.id.activity_matches_stateful_layout)
    public StatefulLayout mStatefulLayout;
    @BindView(R.id.activity_matches_outcomes_recycler_view)
    public RecyclerView mCriteriaRecyclerView;
    @BindView(R.id.activity_matches_matches_recycler_view)
    public RecyclerView mMatchesRecyclerView;

    private MatchAdapter mMatchAdapter;
    private CriteriaAdapter mCriteriaAdapter;

    private String mChapterID;
    private Map<Integer, List<String>> mOutcomeMap;

    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        ButterKnife.bind(this, this);

        init();
        setupRecyclerViews();

        mCriteriaAdapter.setDataSource((List) getTags());
        mMatchesViewModel.fetchMatches(mChapterID, getTags());
    }

    //endregion

    //region Observer members

    @Override
    public void onChanged(Response response) {
        if (response instanceof Response.Loading) {
            if (((Response.Loading) response).isLoading())
                mStatefulLayout.showLoading();
            else
                mStatefulLayout.showContent();
        } else if (response instanceof Response.Error) {
            mStatefulLayout.showError(((Response.Error) response).getMessage(), v -> {
                mMatchesViewModel.fetchMatches(mChapterID, getTags());
            });
        } else {
            Response.Succeed succeed = (Response.Succeed) response;

            if (mMatchAdapter != null)
                mMatchAdapter.setDataSource((List) succeed.getData());
        }
    }

    //endregion

    //region Public members

    public static Intent newIntent(Context context, @NonNull String chapterID, @NonNull Map<Integer,
            List<String>> outcome) {
        Intent returnValue = new Intent(context, MatchesActivity.class);

        returnValue.putExtra(EXTRA_CHAPTER_ID, chapterID);
        returnValue.putExtra(EXTRA_OUTCOME, (HashMap) outcome);

        return returnValue;
    }

    //endregion

    //region Private members

    private void init() {
        mMatchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);
        mMatchesViewModel.observe(this, this);

        extractExtras();
    }

    private void setupRecyclerViews() {
        if (mMatchAdapter == null)
            mMatchAdapter = new MatchAdapter();

        mMatchesRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mMatchesRecyclerView.addItemDecoration(new VerticalSpaceDecorator(getResources().getDimensionPixelSize(R.dimen.spacing_small),
                getResources().getDimensionPixelSize(R.dimen.spacing_small)));
        mMatchesRecyclerView.setAdapter(mMatchAdapter);

        if (mCriteriaAdapter == null)
            mCriteriaAdapter = new CriteriaAdapter();

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);

        mCriteriaRecyclerView.setLayoutManager(flexboxLayoutManager);
        mCriteriaRecyclerView.addItemDecoration(new VerticalSpaceDecorator(getResources().getDimensionPixelSize(R.dimen.spacing_tiny),
                getResources().getDimensionPixelSize(R.dimen.spacing_tiny)));
        mCriteriaRecyclerView.setAdapter(mCriteriaAdapter);
    }

    private void extractExtras() {
        if (getIntent().getExtras() == null)
            throw new IllegalArgumentException("Fatal Error, Missing arguments");

        mChapterID = getIntent().getStringExtra(EXTRA_CHAPTER_ID);
        mOutcomeMap = (HashMap) getIntent().getSerializableExtra(EXTRA_OUTCOME);
    }

    private List<String> getTags() {
        if (mOutcomeMap == null)
            return null;

        List<String> tags = new ArrayList<>();
        for (Map.Entry<Integer, List<String>> entry : mOutcomeMap.entrySet()) {
            tags.addAll(entry.getValue());
        }

        return tags;
    }

    //endregion
}
