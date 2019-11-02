package com.se.matchy.model.chapter;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.base.FireStoreModel;

/**
 * Represents a topic which a user can initiate a surveys to fetch matches based on.
 */
@IgnoreExtraProperties
public class Chapter extends FireStoreModel {

    //region Variables

    private int mNumberOfMatches;
    private String mCode;
    private String mDescription;
    private String mName;

    //endregion

    //region Constructor

    public Chapter() {

    }

    //endregion

    //region Getters && Setters

    public int getNumberOfMatches() {
        return mNumberOfMatches;
    }

    public void setNumberOfMatches(int numberOfMatches) {
        mNumberOfMatches = numberOfMatches;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    //endregion

    //region FireStoreModel members

    @Override
    public String getCollectionName() {
        return Collections.chapters.toString();
    }

    //endregion
}
