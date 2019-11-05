package com.se.matchy.framework.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.se.matchy.BuildConfig;
import com.se.matchy.framework.messages.Response;

/**
 * Base class for all view model
 */
public class AbstractViewModel extends ViewModel {

    //region Private members

    /**
     * Publishes a loading state for all observers on a live data object
     *
     * @param source    source to publish the state on
     * @param isLoading whether its loading or not
     */
    protected void publishLoading(MutableLiveData<Response> source, boolean isLoading) {
        if (source == null)
            return;

        source.setValue(new Response.Loading(isLoading));
    }

    /**
     * Publishes a succeeded state for all observers on a live data object
     *
     * @param source source to publish the state on
     * @param data   what to publish
     */
    protected void publishData(MutableLiveData<Response> source, Object data) {
        publishLoading(source, false);
        if (source == null)
            return;

        source.setValue(new Response.Succeed(data));
    }

    /**
     * Publishes an error state for all observers on a live data source
     *
     * @param source    where to publish
     * @param throwable the throwable with its stack
     * @param message   custom message, preferable throwable message
     */
    protected void publishError(MutableLiveData<Response> source, Throwable throwable, String message) {
        publishLoading(source, false);
        if (source == null)
            return;

        source.setValue(new Response.Error(throwable, message));
    }

    /**
     * Publishes an error state for all observers on a live data source
     *
     * @param source    where to publish
     * @param throwable the throwable with its stack
     */
    protected void publishError(MutableLiveData<Response> source, Throwable throwable) {
        String message = "Please make sure you're using a steady internet connection with VPN enabled on it.";

        if (BuildConfig.DEBUG || throwable instanceof IllegalAccessError)
            message = throwable.getMessage();

        publishError(source, throwable, message);
    }

    //endregion
}
