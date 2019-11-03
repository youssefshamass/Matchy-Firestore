package com.se.matchy.ui.matches.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseAdapter;
import com.se.matchy.framework.ui.BaseViewHolder;
import com.se.matchy.ui.matches.viewholders.MatchViewHolder;

public class MatchAdapter extends BaseAdapter {

    //region BaseAdapter members

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_match, parent, false);
        return new MatchViewHolder(itemView);
    }

    //endregion
}
