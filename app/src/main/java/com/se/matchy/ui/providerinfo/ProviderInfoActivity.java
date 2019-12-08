package com.se.matchy.ui.providerinfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseActivity;
import com.se.matchy.model.serviceprovider.ServiceProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProviderInfoActivity extends BaseActivity {
    //region Constants

    private static final String EXTRA_SERVICE_PROVIDER = ProviderInfoActivity.class.getSimpleName();

    //endregion

    //region Variables

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.activity_provider_info_chapter_indicator_image_view)
    public ImageView mChapterIndicatorImageView;
    @BindView(R.id.activity_provider_info_name_text_view)
    public TextView mNameTextView;
    @BindView(R.id.activity_provider_info_location_detail_text_view)
    public TextView mLocationDetailsTextView;
    @BindView(R.id.activity_provider_info_mobile_number_text_view)
    public TextView mMobileNumberTextView;
    @BindView(R.id.activity_provider_info_email_address_text_view)
    public TextView mEmailAddressTextView;

    private ServiceProvider mServiceProvider;
    //endregion

    //region Activity members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_info);
        ButterKnife.bind(this, this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mServiceProvider = (ServiceProvider) getIntent().getSerializableExtra(EXTRA_SERVICE_PROVIDER);

        bindObject();
    }

    //endregion

    //region Public members

    public static Intent newIntent(Context context, ServiceProvider serviceProvider) {
        Intent returnValue = new Intent(context, ProviderInfoActivity.class);

        returnValue.putExtra(EXTRA_SERVICE_PROVIDER, serviceProvider);

        return returnValue;
    }

    //endregion

    //region Private members

    private void bindObject() {
        mNameTextView.setText(mServiceProvider.getName());
        mLocationDetailsTextView.setText(mServiceProvider.getLocationSpecification());
        mMobileNumberTextView.setText("Mobile Number :" + mServiceProvider.getMobileNumber());
        mEmailAddressTextView.setText("Email Address :" + mServiceProvider.getEmailAddress());

        String drawableResName = "ic_" + mServiceProvider.getChapterID().toLowerCase();
        int drawableResId = getResources().getIdentifier(drawableResName, "drawable", getPackageName());

        if (drawableResId != 0)
            mChapterIndicatorImageView.setImageResource(drawableResId);
    }

    //endregion
}
