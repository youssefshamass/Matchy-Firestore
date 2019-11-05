package com.se.matchy.model.users;

import com.se.matchy.metadata.Collections;
import com.se.matchy.model.base.FireStoreModel;

public class UserMatch extends FireStoreModel {
    //region FireStoreModel members

    @Override
    public String getCollectionName() {
        return Collections.userMatches.toString();
    }

    //endregion

    //region Fields

    public enum Fields {
        userID,
        serviceProviderID
    }

    //endregion
}
