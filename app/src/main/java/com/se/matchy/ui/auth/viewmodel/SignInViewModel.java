package com.se.matchy.ui.auth.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.viewmodel.AbstractViewModel;

import timber.log.Timber;

public class SignInViewModel extends AbstractViewModel {
    //region Variables

    private FirebaseAuth mFirebaseAuth;
    /**
     * User source to log events into
     */
    private MutableLiveData<Response> mUser;

    //endregion

    //region Constructor

    public SignInViewModel() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = new MutableLiveData<>();
    }

    //endregion

    //region Public members

    /**
     * Register an observer on user source.
     *
     * @param lifecycleOwner Fragment|Activity
     * @param observer       observer implementation
     */
    public void registerObserver(LifecycleOwner lifecycleOwner, Observer<Response> observer) {
        mUser.observe(lifecycleOwner, observer);
    }

    /**
     * authenticate a user using Firebase Authentication Library
     *
     * @param emailAddress user's email
     * @param password     user's password
     */
    public void signIn(String emailAddress, String password) {
        publishLoading(mUser, true);

        mFirebaseAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnSuccessListener(authResult -> {
                    if (authResult.getUser() != null) {
                        publishData(mUser, authResult.getUser());
                    } else {
                        publishError(mUser, new Throwable("Connection to server interrupted, " +
                                "Please try again in few moments"));
                    }
                })
                .addOnFailureListener(e -> {
                    Timber.e(e);
                    publishError(mUser, new IllegalAccessError("Invalid username or password"));
                });
    }
    //endregion
}
