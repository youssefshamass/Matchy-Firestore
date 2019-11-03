package com.se.matchy.ui.survey.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseFragment;
import com.se.matchy.model.survey.Question;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionFragment extends BaseFragment implements BlockingStep {

    //region Constants

    private static final String ARG_QUESTION = QuestionFragment.class.getSimpleName() + ".QUESTION";

    //endregion

    //region Variables

    private Question mQuestion;

    @BindView(R.id.fragment_question_header_text_view)
    public TextView mHeaderTextView;
    @BindView(R.id.fragment_question_answers_view_group)
    public ViewGroup mAnswersViewGroup;

    //endregion

    //region Getters

    @Nullable
    public List<String> getSelectedAnswers() {
        List<String> returnValue = null;

        if (mAnswersViewGroup.getChildCount() > 0) {
            returnValue = new ArrayList<>();

            for (int viewIndex = 0; viewIndex < mAnswersViewGroup.getChildCount(); viewIndex++) {
                View view = mAnswersViewGroup.getChildAt(viewIndex);

                if (!(view instanceof CheckBox))
                    continue;

                if (((CheckBox) view).isChecked())
                    returnValue.add(((CheckBox) view).getText().toString());
            }
        }

        return returnValue;
    }

    //endregion

    //region Fragment members

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View returnValue = inflater.inflate(R.layout.fragment_question, container, false);
        ButterKnife.bind(this, returnValue);

        if (getArguments() == null)
            throw new IllegalArgumentException("Fatal Error, Null Arguments");

        //It's better to throw an exception if not found.
        mQuestion = (Question) getArguments().getSerializable(ARG_QUESTION);

        bindQuestion();

        return returnValue;
    }

    //endregion

    //region Stepper members

    @Nullable
    @Override
    public VerificationError verifyStep() {
        List<String> selectedAnswers = getSelectedAnswers();

        if (selectedAnswers == null || selectedAnswers.size() == 0)
            return new VerificationError(getString(R.string.missing_answers_validation_message));

        else return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(), error.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        persistCurrentStepOutcome();

        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        persistCurrentStepOutcome();

        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    //endregion

    //region Public members

    public static Step newInstance(Question question) {
        Fragment returnValue = new QuestionFragment();
        Bundle fragmentArguments = new Bundle();

        fragmentArguments.putSerializable(ARG_QUESTION, question);

        returnValue.setArguments(fragmentArguments);
        return (Step) returnValue;
    }

    //endregion

    //region Private members

    private void bindQuestion() {
        mHeaderTextView.setText(mQuestion.getQuestion());

        if (mAnswersViewGroup.getChildCount() > 0)
            return;

        for (String answer : mQuestion.getAnswers()) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(answer);

            mAnswersViewGroup.addView(checkBox);
        }
    }

    private void persistCurrentStepOutcome() {
        List<String> outcome = getSelectedAnswers();

        if (getActivity() instanceof SurveyActivity)
            ((SurveyActivity) getActivity()).persistOutcome(mQuestion.getOrder(), outcome);
    }

    //endregion
}
