package com.se.matchy.ui.matches.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.viewmodel.AbstractViewModel;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.serviceprovider.ServiceProvider;
import com.se.matchy.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MatchesViewModel extends AbstractViewModel {
    //region Variables

    private MutableLiveData<Response> mMatches;
    private CollectionReference mMatchesCollection;

    //endregion

    //region Constructor

    public MatchesViewModel() {
        mMatches = new MutableLiveData<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mMatchesCollection = db.collection(Collections.serviceProviders.toString());
    }

    //endregion

    //region Public members

    public void observe(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mMatches.observe(lifecycleOwner, observer);
    }

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

    //endregion
}
