package com.se.matchy.model.survey;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.base.FireStoreModel;

import java.util.Date;
import java.util.List;

/**
 * Represents a surveys form related to a single chapter.
 */
@IgnoreExtraProperties
public class Survey extends FireStoreModel {
    //region Variables

    private String mChapterID;
    private Date mFromDate;
    private Date mToDate;
    private List<Question> mQuestions;

    //endregion

    //region Constructor

    public Survey() {
    }

    //endregion

    //region Getters && Setters

    public String getChapterID() {
        return mChapterID;
    }

    public void setChapterID(String chapterID) {
        mChapterID = chapterID;
    }

    public Date getFromDate() {
        return mFromDate;
    }

    public void setFromDate(Date fromDate) {
        mFromDate = fromDate;
    }

    public Date getToDate() {
        return mToDate;
    }

    public void setToDate(Date toDate) {
        mToDate = toDate;
    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(List<Question> questions) {
        mQuestions = questions;
    }

    //endregion

    //region FireStoreModel members

    @Override
    public String getCollectionName() {
        return Collections.surveys.toString();
    }

    //endregion

    //region Fields

    public enum Fields {
        chapterID,
        fromDate
    }

    //endregion
}
