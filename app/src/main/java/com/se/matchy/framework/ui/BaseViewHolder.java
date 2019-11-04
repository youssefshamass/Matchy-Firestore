package com.se.matchy.framework.ui;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Base implementation of RecyclerView.ViewHolder
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    //region Variables

    private Object mDataSource;

    //endregion

    //region Constructor

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    //endregion

    //region Getters

    public Object getDataSource() {
        return mDataSource;
    }

    //endregion

    //region Abstract members

    @CallSuper
    public void bindObject(Object dataSource) {
        mDataSource = dataSource;
    }

    //endregion

    //region OnClickListener

    public interface OnHolderClickListener {
        void onClicked(Object dataSource);
    }

    //endregion
}
