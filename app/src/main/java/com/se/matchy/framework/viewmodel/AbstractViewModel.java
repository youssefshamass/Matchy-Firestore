package com.se.matchy.framework.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.se.matchy.framework.messages.Response;

public class AbstractViewModel extends ViewModel {

    //region Private members

    protected void publishLoading(MutableLiveData<Response> source, boolean isLoading) {
        if (source == null)
            return;

        source.setValue(new Response.Loading(isLoading));
    }

    protected void publishData(MutableLiveData<Response> source, Object data) {
        publishLoading(source, false);
        if (source == null)
            return;

        source.setValue(new Response.Succeed(data));
    }

    protected void publishError(MutableLiveData<Response> source, Throwable throwable, String message) {
        publishLoading(source, false);
        if (source == null)
            return;

        source.setValue(new Response.Error(throwable, message));
    }

    protected void publishError(MutableLiveData<Response> source, Throwable throwable) {
        publishError(source, throwable, throwable.getMessage());
    }

    //endregion
}
