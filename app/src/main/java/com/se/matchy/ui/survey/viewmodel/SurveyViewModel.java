package com.se.matchy.ui.survey.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.viewmodel.AbstractViewModel;
import com.se.matchy.metadata.Collections;
import com.se.matchy.model.survey.Question;
import com.se.matchy.model.survey.Survey;

public class SurveyViewModel extends AbstractViewModel {
    //region Variables

    private CollectionReference mSurveyCollection;
    private MutableLiveData<Response> mSurvey;

    //endregion

    //region Constructor

    public SurveyViewModel() {
        mSurvey = new MutableLiveData<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mSurveyCollection = db.collection(Collections.surveys.toString());
    }

    //endregion

    //region Public members

    public void observeSurvey(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mSurvey.observe(lifecycleOwner, observer);
    }

    public void onSurveyNeeded(String chapterID) {
        publishLoading(mSurvey, true);

        mSurveyCollection.whereEqualTo(Survey.Fields.chapterID.toString(), chapterID)
                .orderBy(Survey.Fields.fromDate.toString())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference documentReference = documentSnapshot.getReference();
                        final Survey survey = documentSnapshot.toObject(Survey.class);

                        documentReference.collection(Collections.questions.toString())
                                .orderBy(Question.Fields.order.toString())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                    survey.setQuestions(queryDocumentSnapshots1.toObjects(Question.class));

                                    publishData(mSurvey, survey);
                                })
                                .addOnFailureListener(e -> {
                                    publishError(mSurvey, e);
                                });

                    }
                })
                .addOnFailureListener(e -> {
                    publishError(mSurvey, e);
                });
    }

    //endregion
}
