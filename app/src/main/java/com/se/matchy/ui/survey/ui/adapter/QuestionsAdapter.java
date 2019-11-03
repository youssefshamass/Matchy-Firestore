package com.se.matchy.ui.survey.ui.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.se.matchy.model.survey.Question;
import com.se.matchy.ui.survey.ui.QuestionFragment;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

import java.util.List;

public class QuestionsAdapter extends AbstractFragmentStepAdapter {
    //region Variables

    private List<Question> mDataSource;

    //endregion

    //region Constructor

    public QuestionsAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    //endregion

    //region Setters

    public void setDataSource(List<Question> questions) {
        mDataSource = questions;

        notifyDataSetChanged();
    }

    //endregion

    //region AbstractFragmentStepAdapter members

    @Override
    public Step createStep(int position) {
        return QuestionFragment.newInstance(mDataSource.get(position));
    }

    @Override
    public int getCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    //endregion
}
