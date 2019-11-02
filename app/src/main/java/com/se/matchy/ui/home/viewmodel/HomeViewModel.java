package com.se.matchy.ui.home.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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

    private MutableLiveData<Response> mChapters;
    private CollectionReference mChaptersCollection;
    private CollectionReference mUserMatchesCollection;

    //endregion

    //region Constructor

    public HomeViewModel() {
        mChapters = new MutableLiveData<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mChaptersCollection = db.collection(Collections.chapters.toString());
        mUserMatchesCollection = db.collection(Collections.userMatches.toString());
    }

    //endregion

    //region Public members

    public void onViewFinishedLoading() {
        fetchChapters();
        fetchUserMatches();
    }

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

    public void fetchUserMatches() {
        //TODO: Impl
    }


    public void observerChapters(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mChapters.observe(lifecycleOwner, observer);
    }

    //endregion
}
