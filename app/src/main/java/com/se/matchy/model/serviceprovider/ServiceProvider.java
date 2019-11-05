package com.se.matchy.model.serviceprovider;

import com.google.firebase.firestore.IgnoreExtraProperties;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.base.FireStoreModel;

import java.util.List;

/**
 * POJO that holds the matches data.
 */
@IgnoreExtraProperties
public class ServiceProvider extends FireStoreModel {
    //region Variables

    private String mChapterID;
    private String mName;
    private String mMajor;
    private List<String> mTags;

    /*
    Matching weight
     */
    private int mWeight;

    //endregion

    //region Constructor

    public ServiceProvider() {
    }

    //endregion

    //region Getters && Setters

    public String getChapterID() {
        return mChapterID;
    }

    public void setChapterID(String chapterID) {
        mChapterID = chapterID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getMajor() {
        return mMajor;
    }

    public void setMajor(String major) {
        mMajor = major;
    }

    public List<String> getTags() {
        return mTags;
    }

    public void setTags(List<String> tags) {
        mTags = tags;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    //endregion

    //region FireStoreModel members

    @Override
    public String getCollectionName() {
        return Collections.serviceProviders.toString();
    }

    //endregion

    //region Fields

    public enum Fields {
        chapterID,
        tags,
        uid
    }

    //endregion
}
