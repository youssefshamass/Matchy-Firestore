package com.se.matchy.ui.matches.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseViewHolder;
import com.se.matchy.model.serviceprovider.ServiceProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchViewHolder extends BaseViewHolder {

    //region Variables

    @BindView(R.id.list_item_match_name_text_view)
    public TextView mNameTextView;
    @BindView(R.id.list_item_match_major_text_view)
    public TextView mMajorTextView;

    //endregion

    //region Constructor

    public MatchViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //endregion

    //region BaseViewHolder members

    @Override
    public void bindObject(Object dataSource) {
        super.bindObject(dataSource);

        if (dataSource == null)
            return;
        if (!(dataSource instanceof ServiceProvider))
            return;

        ServiceProvider serviceProvider = (ServiceProvider) dataSource;

        mNameTextView.setText(serviceProvider.getName());
        mMajorTextView.setText(serviceProvider.getMajor());
    }


    //endregion
}
