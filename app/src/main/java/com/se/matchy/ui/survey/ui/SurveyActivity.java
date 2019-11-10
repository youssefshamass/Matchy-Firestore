package com.se.matchy.ui.survey.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.QuickContactBadge;
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
import com.se.matchy.ui.matches.ui.MatchesActivity;
import com.se.matchy.ui.survey.ui.adapter.QuestionsAdapter;
import com.se.matchy.ui.survey.viewmodel.SurveyViewModel;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurveyActivity extends BaseActivity implements Observer<Response>, StepperLayout.StepperListener {
    //region Constants

    private static final String EXTRA_CHAPTER_ID = SurveyActivity.class.getSimpleName() + ".CHAPTER_ID";

    //endregion

    //region Variables

    /**
     * Represents the selected answers for each of the questions
     * Mapped with the question order to prevent outcomes.
     */
    private Map<Integer, List<String>> mOutCome;

    /**
     * Views
     */
    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.activity_survey_stateful_layout)
    public StatefulLayout mStatefulLayout;
    @BindView(R.id.activity_survey_stepper_layout)
    public StepperLayout mStepperLayout;
    private SurveyViewModel mSurveyViewModel;


    private String mChapterID;
    private QuestionsAdapter mQuestionsAdapter;

    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        ButterKnife.bind(this, this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        setupStepper();

        mSurveyViewModel.onSurveyNeeded(mChapterID);
    }

    ///endregion

    //region Observer members

    @Override
    public void onChanged(Response response) {
        if (response instanceof Response.Succeed) {
            Response.Succeed succeed = (Response.Succeed) response;

            if (succeed.getData() == null) {
                mStatefulLayout.showOffline(getString(R.string.offline_warning_message), v -> {
                    mSurveyViewModel.onSurveyNeeded(mChapterID);
                });
            } else {
                Survey survey = (Survey) succeed.getData();

                if (mQuestionsAdapter == null)
                    mQuestionsAdapter = new QuestionsAdapter(getSupportFragmentManager(), this);

                mQuestionsAdapter.setDataSource(survey.getQuestions());
            }
        } else if (response instanceof Response.Loading) {
            if (((Response.Loading) response).isLoading()) {
                mStatefulLayout.showLoading();
            } else {
                mStatefulLayout.showContent();
            }
        } else if (response instanceof Response.Error) {
            mStatefulLayout.showError(((Response.Error) response).getMessage(), v -> {
                mSurveyViewModel.onSurveyNeeded(mChapterID);
            });
        }
    }

    //endregion

    //region StepperListener members

    @Override
    public void onCompleted(View completeButton) {
        Intent intent = MatchesActivity.newIntent(this, mChapterID, mOutCome);
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {

    }

    //endregion

    //region Public members

    public static Intent newIntent(Context context, String chapterID) {
        Intent returnValue = new Intent(context, SurveyActivity.class);

        returnValue.putExtra(EXTRA_CHAPTER_ID, chapterID);

        return returnValue;
    }

    public void persistOutcome(Integer questionOrder, List<String> outcomes) {
        if (mOutCome == null)
            mOutCome = new HashMap<>();

        List<String> answers = new ArrayList<>();
        mOutCome.put(questionOrder, answers);

        answers.addAll(outcomes);
    }

    //endregion

    //region Private members

    private void init() {
        mSurveyViewModel = ViewModelProviders.of(this).get(SurveyViewModel.class);
        mSurveyViewModel.observeSurvey(this, this);

        extractExtras();
        setupStepper();
    }

    private void setupStepper() {
        if (mQuestionsAdapter == null)
            mQuestionsAdapter = new QuestionsAdapter(getSupportFragmentManager(), this);

        mStepperLayout.setListener(this);
        mStepperLayout.setAdapter(mQuestionsAdapter);
    }

    private void extractExtras() {
        if (getIntent().getExtras() == null)
            throw new IllegalArgumentException("Fatal Error, Missing Arguments");

        mChapterID = getIntent().getStringExtra(EXTRA_CHAPTER_ID);
    }

    //endregion

}
