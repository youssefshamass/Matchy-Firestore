package com.se.matchy.ui.home.viewmodel;

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
import com.se.matchy.model.chapter.Chapter;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class HomeViewModel extends AbstractViewModel {
    //region Variables

    /**
     * Chapters source to log events into
     */
    private MutableLiveData<Response> mChapters;
    /*
        Direct reference to chapters collection in FireStore
     */
    private CollectionReference mChaptersCollection;
    /*
        Direct reference to UserMatches collection in FireStore
     */
    private CollectionReference mUserMatchesCollection;

    /*
        FireBase Authentication Client.
     */
    private FirebaseAuth mFirebaseAuth;

    //endregion

    //region Constructor

    public HomeViewModel() {
        mChapters = new MutableLiveData<>();

        mFirebaseAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mChaptersCollection = db.collection(Collections.chapters.toString());
        mUserMatchesCollection = db.collection(Collections.userMatches.toString());
    }

    //endregion

    //region Public members

    /**
     * Registers an observer which gets notified at any change on the chapters source.
     * @param lifecycleOwner Activity|Fragment
     * @param observer observer implementation
     */
    public void observerChapters(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mChapters.observe(lifecycleOwner, observer);
    }

    /**
     * Initializes the view with the appropriate data from FireStore
     * A List Of Chapters
     * A List of User Matches in case the user is Logged in
     */
    public void onViewFinishedLoading() {
        fetchChapters();
        fetchUserMatches();
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
     * Query FireStore UserMatches collection
     */
    public void fetchUserMatches() {
        //TODO: Impl
    }

    /**
     * Returns the cached user from FireBase Auth
      * @return Firebase User Object
     */
    public FirebaseUser getLoggedInUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    /**
     * Do a null check over cached user to check if it's logged in or not
     * @return boolean indicating weather is logged in or not
     */
    public boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    //endregion
}
