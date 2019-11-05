package com.se.matchy.ui.home.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.se.matchy.R;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.ui.BaseActivity;
import com.se.matchy.framework.ui.BaseViewHolder;
import com.se.matchy.framework.ui.decorators.HorizontalEdgeDecorator;
import com.se.matchy.framework.ui.decorators.HorizontalSpaceDecorator;
import com.se.matchy.model.chapter.Chapter;
import com.se.matchy.ui.auth.ui.SignInActivity;
import com.se.matchy.ui.auth.ui.SignUpActivity;
import com.se.matchy.ui.home.ui.adapters.ChapterAdapter;
import com.se.matchy.ui.home.viewmodel.HomeViewModel;
import com.se.matchy.ui.survey.ui.SurveyActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class HomeActivity extends BaseActivity {

    //region Variables

    private HomeViewModel mHomeViewModel;

    @BindView(R.id.activity_main_topics_recycler_view)
    public ShimmerRecyclerView mChaptersRecyclerView;
    @BindView(R.id.activity_main_recent_matches_recycler_view)
    public RecyclerView mRecentMatchesRecyclerView;
    @BindView(R.id.activity_home_lack_permission_view_group)
    public ViewGroup mLackOfPermissionsViewGroup;

    private FirebaseUser mFirebaseUser;
    private ChapterAdapter mChapterAdapter;

    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this, this);

        init();

        mHomeViewModel.onViewFinishedLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mFirebaseUser = mHomeViewModel.getLoggedInUser();

        updateUIBasedOnUserStatus();
    }

    //endregion

    //region Listeners

    @OnClick(R.id.activity_home_sign_in_button)
    public void onSignInClicked() {
        Intent intent = SignInActivity.newIntent(this);
        startActivity(intent);
    }

    //endregion

    //region Private members

    private void init() {
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        mHomeViewModel.observerChapters(this, response -> {
            if (response instanceof Response.Loading) {
                if (((Response.Loading) response).isLoading())
                    mChaptersRecyclerView.showShimmerAdapter();
                else
                    mChaptersRecyclerView.hideShimmerAdapter();
            } else if (response instanceof Response.Error) {

            } else {
                Response.Succeed succeed = (Response.Succeed) response;
                if (succeed.getData() != null)
                    mChapterAdapter.setDataSource((List) succeed.getData());
            }
        });

        setupRecyclerViews();
    }

    private void setupRecyclerViews() {
        if (mChapterAdapter == null) {
            mChapterAdapter = new ChapterAdapter();
            mChapterAdapter.setHolderClickListener(dataSource -> {
                if (dataSource == null)
                    return;

                if (!(dataSource instanceof Chapter))
                    return;

                Intent intent = SurveyActivity.newIntent(this, ((Chapter) dataSource).getUid());
                startActivity(intent);
            });
        }

        mChaptersRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mChaptersRecyclerView.addItemDecoration(new HorizontalSpaceDecorator(getResources().getDimensionPixelSize(
                R.dimen.spacing_normal)));
        mChaptersRecyclerView.addItemDecoration(new HorizontalEdgeDecorator(getResources().getDimensionPixelOffset(
                R.dimen.spacing_normal
        )));
        mChaptersRecyclerView.setAdapter(mChapterAdapter);
    }

    private void updateUIBasedOnUserStatus() {
        if (mFirebaseUser != null) {
            mLackOfPermissionsViewGroup.setVisibility(View.GONE);
        } else {
            mLackOfPermissionsViewGroup.setVisibility(ViewGroup.VISIBLE);
        }
    }
    //endregion

    //region Public members

    public static Intent newIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    //endregion
}
