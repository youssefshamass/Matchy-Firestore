package com.se.matchy.ui.matches.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CriterionViewHolder extends BaseViewHolder {

    //region Variables

    @BindView(R.id.list_item_criteria_text_view)
    public TextView mTextView;

    //endregion

    //region Constructor

    public CriterionViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //endregion

    //region Public members

    @Override
    public void bindObject(Object dataSource) {
        super.bindObject(dataSource);

        mTextView.setText(dataSource.toString());
    }


    //endregion
}
