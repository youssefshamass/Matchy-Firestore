package com.se.matchy.ui.home.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseAdapter;
import com.se.matchy.framework.ui.BaseViewHolder;
import com.se.matchy.ui.home.ui.viewholders.ChapterViewHolder;

public class ChapterAdapter extends BaseAdapter {

    //region RecyclerView.Adapter members

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chapter, parent, false);
        return new ChapterViewHolder(itemView);
    }

    //endregion
}
