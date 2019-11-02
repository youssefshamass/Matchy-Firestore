package com.se.matchy.framework.ui;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    //region Variables

    private List<Object> mDataSource;
    private BaseViewHolder.OnHolderClickListener mHolderClickListener;

    //endregion

    //region RecyclerView.Adapter members

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bindObject(mDataSource.get(position));

        if (mHolderClickListener != null)
            holder.itemView.setOnClickListener(v -> {
                mHolderClickListener.onClicked(holder.getDataSource());
            });
    }

    @Override
    public int getItemCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    //endregion

    //region Setters members

    public void setDataSource(List<Object> dataSource) {
        mDataSource = dataSource;

        notifyDataSetChanged();
    }

    public void setHolderClickListener(BaseViewHolder.OnHolderClickListener holderClickListener) {
        mHolderClickListener = holderClickListener;
    }

    //endregion
}
