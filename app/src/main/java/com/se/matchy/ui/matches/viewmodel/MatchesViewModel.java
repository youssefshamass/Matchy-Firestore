package com.se.matchy.ui.matches.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.viewmodel.AbstractViewModel;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.serviceprovider.ServiceProvider;
import com.se.matchy.model.users.UserMatch;
import com.se.matchy.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulation of the MatchesActivity Presentation logic.
 */
public class MatchesViewModel extends AbstractViewModel {
    //region Variables

    private FirebaseAuth mFirebaseAuth;
    //Matches collection reference
    private CollectionReference mMatchesCollection;
    //User matches collection reference
    private CollectionReference mUserMatchesCollection;
    //Matches source
    private MutableLiveData<Response> mMatches;

    //endregion

    //region Constructor

    public MatchesViewModel() {
        mMatches = new MutableLiveData<>();

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mMatchesCollection = db.collection(Collections.serviceProviders.toString());
        mUserMatchesCollection = db.collection(Collections.userMatches.toString());
    }

    //endregion

    //region Public members

    /**
     * Register an observer for any changes upon matches source.
     *
     */
    public void observe(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mMatches.observe(lifecycleOwner, observer);
    }

    /**
     * Fetches survey's topic service providers and then try to pick and weight the appropriate providers
     * using the mandatoryTags (to Pick) and the tags (to weight) fields.
     * @param chapterID survey's chapter id
     * @param tags a list of the selected answers by the user
     */
    public void fetchMatches(String chapterID, List<String> tags) {
        publishLoading(mMatches, true);

        mMatchesCollection
                .whereEqualTo(ServiceProvider.Fields.chapterID.toString(), chapterID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ServiceProvider> serviceProviders = null;
                    List<ServiceProvider> validServiceProviders = null;

                    if (!queryDocumentSnapshots.isEmpty()) {
                        validServiceProviders = new ArrayList<>();
                        serviceProviders = queryDocumentSnapshots.toObjects(ServiceProvider.class);

                        for (ServiceProvider serviceProvider : serviceProviders) {
                            int weight = 0;

                            if (serviceProvider.getMandatoryTags() != null &&
                                    serviceProvider.getMandatoryTags().size() > 0) {
                                boolean passedMatchCheck = true;

                                for (String mandatoryTag : serviceProvider.getMandatoryTags()) {
                                    boolean passedDeconstructedMatchCheck = false;
                                    List<String> deconstructedTags = new ArrayList<>();

                                    //The tag contains an or operator
                                    if (mandatoryTag.contains("||"))
                                        deconstructedTags.addAll(Arrays.asList(mandatoryTag.split("||")));
                                    else
                                        deconstructedTags.add(mandatoryTag);


                                    for (String tag : deconstructedTags)
                                        passedDeconstructedMatchCheck |= GeneralUtils.isStringInList(tags, tag);


                                    if(!passedDeconstructedMatchCheck) {
                                        passedMatchCheck = false;
                                        break;
                                    }
                                }

                                if (!passedMatchCheck)
                                    continue;
                            }

                            for (String tag : tags) {
                                if (GeneralUtils.isStringInList(serviceProvider.getTags(), tag))
                                    weight++;
                            }


                            if (weight > 0) {
                                serviceProvider.setWeight(weight);
                                validServiceProviders.add(serviceProvider);
                            }
                        }
                    }

                    if (validServiceProviders.size() > 0) {
                        java.util.Collections.sort(validServiceProviders, (o1, o2) -> o1.getWeight() - o2.getWeight());
                        java.util.Collections.reverse(validServiceProviders);
                    }

                    publishData(mMatches, validServiceProviders);
                })
                .addOnFailureListener(e -> {
                    publishError(mMatches, e);
                });

    }

    /**
     * Persist results as user recent matches
     * @param firebaseUser logged in user
     * @param serviceProviders list of matches
     */
    public void persistMatches(FirebaseUser firebaseUser, List<ServiceProvider> serviceProviders) {
        if (firebaseUser == null)
            return;
        if (serviceProviders == null || serviceProviders.size() == 0)
            return;

        for (ServiceProvider serviceProvider : serviceProviders) {
            Map<String, Object> requestParams = new HashMap<>();
            requestParams.put(UserMatch.Fields.userID.toString(), firebaseUser.getUid());
            requestParams.put(UserMatch.Fields.serviceProviderID.toString(), serviceProvider.getUid());

            mUserMatchesCollection.document(firebaseUser.getUid() + "_" + serviceProvider.getUid())
                    .set(requestParams);
        }
    }

    public boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    public FirebaseUser getLoggedInUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    //endregion
}
