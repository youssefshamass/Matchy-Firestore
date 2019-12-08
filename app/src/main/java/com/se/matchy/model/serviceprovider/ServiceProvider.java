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

    //To Which Topic or Chapter this service provider is related to
    private String mChapterID;
    //Provider name; used for display only
    private String mName;
    //Field of work; used for display only
    private String mMajor;
    //used for display only
    private String mFloor;
    //used for display only
    private String mLocationDetails;
    //used for display only
    private String mMobileNumber;
    //used for display only
    private String mEmailAddress;
    //List of keys used to boost the order of the service provider when multiple matches are found.
    private List<String> mTags;
    //List of keys that the provider must have in order to appear as a match.
    private List<String> mMandatoryTags;
    //Match weight; the results are ordered descending based on the value of this field.
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

    public List<String> getMandatoryTags() {
        return mMandatoryTags;
    }

    public void setMandatoryTags(List<String> mandatoryTags) {
        mMandatoryTags = mandatoryTags;
    }

    public int getWeight() {
        return mWeight;
    }

    public void setWeight(int weight) {
        mWeight = weight;
    }

    public String getFloor() {
        return mFloor;
    }

    public void setFloor(String floor) {
        mFloor = floor;
    }

    public String getLocationDetails() {
        return mLocationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        mLocationDetails = locationDetails;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getLocationSpecification() {
        return "Found At " + getLocationDetails() + " - Floor " + getFloor();
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
