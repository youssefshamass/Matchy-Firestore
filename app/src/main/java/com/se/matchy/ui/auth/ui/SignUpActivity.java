package com.se.matchy.ui.auth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gturedi.views.StatefulLayout;
import com.se.matchy.R;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.ui.BaseActivity;
import com.se.matchy.ui.auth.viewmodel.SignUpViewModel;
import com.se.matchy.ui.home.ui.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements Observer<Response> {
    //region Variables

    private SignUpViewModel mSignUpViewModel;

    @BindView(R.id.activity_signup_stateful_layout)
    public StatefulLayout mStatefulLayout;
    @BindView(R.id.activity_signup_email_edit_text)
    public EditText mEmailEditText;
    @BindView(R.id.activity_signup_password_edit_text)
    public EditText mPasswordEditText;
    @BindView(R.id.activity_signup_password_confirmation_edit_text)
    public EditText mConfirmPasswordEditText;

    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this, this);

        init();
    }

    //endregion

    //region Listeners

    @OnClick(R.id.activity_signup_submit_button)
    public void onSubmitClicked() {
        if (!validateAction())
            return;

        mSignUpViewModel.signUpUser(mEmailEditText.getText().toString(),
                mPasswordEditText.getText().toString());
    }

    //endregion

    //region Observer members

    /**
     * Gets called when new changes are made to the SignUp View Model's user source.
     *
     * @param response Either Loading, Error, Or Succeeded.
     */
    @Override
    public void onChanged(Response response) {
        if (response instanceof Response.Loading) {
            if (((Response.Loading) response).isLoading())
                mStatefulLayout.showLoading();
            else
                mStatefulLayout.showContent();
        } else if (response instanceof Response.Error) {
            Toast.makeText(this, ((Response.Error) response).getMessage()
                    , Toast.LENGTH_SHORT).show();
        } else {
            Response.Succeed succeed = (Response.Succeed) response;

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null) {
                Toast.makeText(this, R.string.successfully_signed_in, Toast.LENGTH_SHORT).show();
                redirectToHome();
            }
        }

    }

    private void redirectToHome() {
        Intent homeIntent = HomeActivity.newIntent(this);
        startActivity(homeIntent);
        finish();
    }

    //endregion

    //region Private members

    private void init() {
        mSignUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        mSignUpViewModel.registerObserver(this, this);
    }

    private boolean validateAction() {
        boolean returnValue = true;

        if (TextUtils.isEmpty(mEmailEditText.getText())) {
            returnValue = false;
            mEmailEditText.setError(getString(R.string.required));
        }

        if (TextUtils.isEmpty(mPasswordEditText.getText())) {
            returnValue = false;
            mPasswordEditText.setError(getString(R.string.required));
        } else if (mPasswordEditText.getText().toString().length() < 6) {
            returnValue = false;
            mPasswordEditText.setError(getString(R.string.weak_password_validation_message));
        }

        if (TextUtils.isEmpty(mConfirmPasswordEditText.getText())) {
            returnValue = false;
            mConfirmPasswordEditText.setError(getString(R.string.required));
        }

        if (returnValue) {
            String password = mPasswordEditText.getText().toString();
            String passwordConfirmation = mConfirmPasswordEditText.getText().toString();

            if (!password.equals(passwordConfirmation)) {
                returnValue = false;
                mConfirmPasswordEditText.setError(getString(R.string.password_mismatch_validation_message));
            }
        }

        return returnValue;
    }

    //endregion

    //region Public members

    public static Intent newIntent(Context context) {
        return new Intent(context, SignUpActivity.class);
    }

    //endregion
}
