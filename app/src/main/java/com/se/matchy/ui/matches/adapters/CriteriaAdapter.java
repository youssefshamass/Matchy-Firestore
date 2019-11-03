package com.se.matchy.ui.matches.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.se.matchy.R;
import com.se.matchy.framework.ui.BaseAdapter;
import com.se.matchy.framework.ui.BaseViewHolder;
import com.se.matchy.ui.matches.viewholders.CriterionViewHolder;

public class CriteriaAdapter extends BaseAdapter {
    //region RecyclerView.Adapter members

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_criteria, parent, false);
        return new CriterionViewHolder(itemView);
    }

    //endregion
}
