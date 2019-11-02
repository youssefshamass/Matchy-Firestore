package com.se.matchy.model.survey;

import com.se.matchy.metadata.Collections;
import com.se.matchy.model.base.FireStoreModel;

import java.util.List;

/**
 * Simple Question POJO
 */
public class Question extends FireStoreModel {

    //region Variables

    private String mQuestion;
    private List<String> mAnswers;
    private int mOrder;

    //endregion

    //region Constructor

    public Question() {
    }

    //endregion

    //region Getters && Setters

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public List<String> getAnswers() {
        return mAnswers;
    }

    public void setAnswers(List<String> answers) {
        mAnswers = answers;
    }

    public int getOrder() {
        return mOrder;
    }

    public void setOrder(int order) {
        mOrder = order;
    }

    //endregion

    //region FireStoreModel members

    @Override
    public String getCollectionName() {
        return Collections.questions.toString();
    }

    //endregion

    //region Fields

    public enum Fields {
        order
    }

    //endregion
}
