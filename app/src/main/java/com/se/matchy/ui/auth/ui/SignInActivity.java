package com.se.matchy.ui.auth.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gturedi.views.StatefulLayout;
import com.se.matchy.R;
import com.se.matchy.framework.messages.Response;
import com.se.matchy.framework.ui.BaseActivity;
import com.se.matchy.ui.auth.viewmodel.SignInViewModel;
import com.se.matchy.ui.home.ui.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends BaseActivity implements Observer<Response> {
    //region Variables

    private SignInViewModel mSignInViewModel;

    @BindView(R.id.activity_signin_stateful_layout)
    public StatefulLayout mStatefulLayout;
    @BindView(R.id.activity_signin_email_edit_text)
    public EditText mEmailAddressEditText;
    @BindView(R.id.activity_signin_password_edit_text)
    public EditText mPasswordEditText;

    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

        if (mSignInViewModel.isLoggedIn())
            redirectToHome();

        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this, this);
    }

    //endregion

    //region Listeners

    @OnClick(R.id.activity_signin_submit_button)
    public void onSignInClicked() {
        if (!validateAction())
            return;

        mSignInViewModel.signIn(mEmailAddressEditText.getText().toString(),
                mPasswordEditText.getText().toString());
    }

    @OnClick(R.id.activity_sign_in_sign_up_text_view)
    public void onSignUpClicked() {
        Intent signupIntent = SignUpActivity.newIntent(this);
        signupIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(signupIntent);
    }

    //endregion

    //region Observer members

    @Override
    public void onChanged(Response response) {
        if (response instanceof Response.Loading) {
            if (((Response.Loading) response).isLoading())
                mStatefulLayout.showLoading();
            else
                mStatefulLayout.showContent();
        } else if (response instanceof Response.Error) {
            Toast.makeText(this, ((Response.Error) response).getMessage(), Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, R.string.successfully_signed_in, Toast.LENGTH_SHORT).show();
            redirectToHome();
        }
    }

    //endregion

    //region Private members

    private void init() {
        mSignInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        mSignInViewModel.registerObserver(this, this);
    }

    private boolean validateAction() {
        boolean returnValue = true;

        if (TextUtils.isEmpty(mEmailAddressEditText.getText())) {
            returnValue = false;
            mEmailAddressEditText.setError(getString(R.string.required));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailAddressEditText.getText().toString()).matches()) {
            returnValue = false;
            mEmailAddressEditText.setError(getString(R.string.invalid_format));
        }

        if (TextUtils.isEmpty(mPasswordEditText.getText())) {
            returnValue = false;
            mEmailAddressEditText.setError(getString(R.string.required));
        }

        return returnValue;
    }

    private void redirectToHome() {
        Intent homeIntent = HomeActivity.newIntent(this);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(homeIntent);
        finish();
    }

    //endregion

    //region Public members

    public static Intent newIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    //endregion
}
