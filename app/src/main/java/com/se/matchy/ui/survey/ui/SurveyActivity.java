package com.se.matchy.ui.survey.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gturedi.views.StatefulLayout;
import com.se.matchy.R;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.ui.BaseActivity;
import com.se.matchy.model.survey.Survey;
import com.se.matchy.ui.survey.viewmodel.SurveyViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends BaseActivity implements Observer<Response> {
    //region Constants

    private static final String EXTRA_CHAPTER_ID = SurveyActivity.class.getSimpleName() + ".CHAPTER_ID";

    //endregion

    //region Variables

    private SurveyViewModel mSurveyViewModel;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.activity_survey_stateful_layout)
    public StatefulLayout mStatefulLayout;

    private String mChapterID;

    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this, this);

        init();
        mSurveyViewModel.onSurveyNeeded(mChapterID);
    }

    ///endregion

    //region Observer members

    @Override
    public void onChanged(Response response) {
        if (response instanceof Response.Succeed) {
            Response.Succeed succeed = (Response.Succeed) response;
            Survey survey = (Survey) succeed.getData();
        }
    }

    //endregion

    //region Public members

    public static Intent newIntent(Context context, String chapterID) {
        Intent returnValue = new Intent(context, SurveyActivity.class);

        returnValue.putExtra(EXTRA_CHAPTER_ID, chapterID);

        return returnValue;
    }

    //endregion

    //region Private members

    private void init() {
        mSurveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        mSurveyViewModel.observeSurvey(this, this);

        extractExtras();
    }

    private void extractExtras() {
        if (getIntent().getExtras() == null)
            throw new IllegalArgumentException("Fatal Error, Missing Arguments");

        mChapterID = getIntent().getStringExtra(EXTRA_CHAPTER_ID);
    }

    //endregion

}
