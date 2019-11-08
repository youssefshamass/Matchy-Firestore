package com.se.matchy.ui.home.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.viewmodel.AbstractViewModel;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.chapter.Chapter;
import com.se.matchy.model.serviceprovider.ServiceProvider;
import com.se.matchy.model.users.UserMatch;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class HomeViewModel extends AbstractViewModel {
    //region Variables

    /**
     * Chapters source to log events into
     */
    private MutableLiveData<Response> mChapters;
    /**
     * Previously matched service providers source to log events into
     */
    private MutableLiveData<Response> mUserMatches;
    /*
        Direct reference to chapters collection in FireStore
     */
    private CollectionReference mChaptersCollection;
    /*
        Direct reference to UserMatch collection in FireStore
     */
    private CollectionReference mUserMatchesCollection;
    /*
        Direct reference to ServiceProviders collection in FireStore
     */
    private CollectionReference mServiceProvidersCollection;

    /*
        FireBase Authentication Client.
     */
    private FirebaseAuth mFirebaseAuth;

    //endregion

    //region Constructor

    public HomeViewModel() {
        mChapters = new MutableLiveData<>();
        mUserMatches = new MutableLiveData<>();

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mChaptersCollection = db.collection(Collections.chapters.toString());
        mUserMatchesCollection = db.collection(Collections.userMatches.toString());
        mServiceProvidersCollection = db.collection(Collections.serviceProviders.toString());
    }

    //endregion

    //region Public members

    /**
     * Registers an observer which gets notified at any change on the chapters source.
     *
     * @param lifecycleOwner Activity|Fragment
     * @param observer       observer implementation
     */
    public void observerChapters(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mChapters.observe(lifecycleOwner, observer);
    }

    /**
     * Registers an observer which gets notified at any change on the recent matches source.
     *
     * @param lifecycleOwner Activity|Fragment
     * @param observer       observer implementation
     */
    public void observeMatches(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mUserMatches.observe(lifecycleOwner, observer);
    }

    /**
     * Initializes the view with the appropriate data from FireStore
     * A List Of Chapters
     * A List of User Matches in case the user is Logged in
     */
    public void onViewFinishedLoading() {
        fetchChapters();

        FirebaseUser firebaseUser = getLoggedInUser();
    }

    /**
     * Query FireStore Chapters collection
     */
    public void fetchChapters() {
        publishLoading(mChapters, true);

        mChaptersCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Chapter> chapters = new ArrayList<>();

                    if (!queryDocumentSnapshots.isEmpty()) {
                        chapters = queryDocumentSnapshots.toObjects(Chapter.class);
                    }

                    publishData(mChapters, chapters);
                })
                .addOnFailureListener(e -> {
                    Timber.e(e);
                    publishError(mChapters, e);
                });
    }

    /**
     * Query FireStore UserMatch collection
     */
    public void fetchUserMatches(FirebaseUser firebaseUser) {
        if (firebaseUser == null)
            return;

        publishLoading(mUserMatches, true);

        mUserMatchesCollection.whereEqualTo(UserMatch.Fields.userID.toString(), firebaseUser.getUid())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    final List<ServiceProvider> serviceProviders = new ArrayList<>();
                    if (!queryDocumentSnapshots.isEmpty()) {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String serviceProviderID = (String) documentSnapshot.
                                    get(UserMatch.Fields.serviceProviderID.toString());

                            mServiceProvidersCollection
                                    .whereEqualTo(ServiceProvider.Fields.uid.toString(), serviceProviderID)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        DocumentSnapshot serviceProviderDocument = queryDocumentSnapshots1.getDocuments()
                                                .get(0);
                                        ServiceProvider serviceProvider = serviceProviderDocument
                                                .toObject(ServiceProvider.class);

                                        serviceProviders.add(serviceProvider);
                                        publishData(mUserMatches, serviceProviders);
                                    })
                                    .addOnFailureListener(e -> {
                                        Timber.e(e);
                                    });
                        }
                    }
                });
    }

    /**
     * Returns the cached user from FireBase Auth
     *
     * @return Firebase User Object
     */
    public FirebaseUser getLoggedInUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    /**
     * Do a null check over cached user to check if it's logged in or not
     *
     * @return boolean indicating weather is logged in or not
     */
    public boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    //endregion
}
