package com.se.matchy.model.base;

/**
 * Base FireStore Models class which defines the parameters needed to connect to a certain collection
 * in Firebase FireStore
 */
public abstract class FireStoreModel extends Model {
    //region Variables

    private String mUid;

    //endregion

    //region Constructor

    public FireStoreModel() {
    }

    //endregion

    //region Getters && Setters

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    //endregion

    //region Abstract members

    /**
     * Defines the collection which the model is related to.
     *
     * @return related collection name
     */
    public abstract String getCollectionName();

    //endregion
}
