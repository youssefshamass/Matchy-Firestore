package com.se.matchy.ui.home.ui.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseViewHolder;
import com.se.matchy.model.chapter.Chapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChapterViewHolder extends BaseViewHolder {
    //region Variables

    @BindView(R.id.list_item_chapter_title_text_view)
    public TextView mTitleTextView;
    @BindView(R.id.list_item_chapter_description_text_view)
    public TextView mDescriptionTextView;

    //endregion

    //region Constructor

    public ChapterViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    //endregion

    //region BaseViewHolder members

    @Override
    public void bindObject(Object dataSource) {
        super.bindObject(dataSource);

        if (dataSource == null) return;
        if (!(dataSource instanceof Chapter)) return;

        Chapter chapter = (Chapter) dataSource;

        mTitleTextView.setText(chapter.getName());
        mDescriptionTextView.setText(chapter.getDescription());
    }


    //endregion
}
