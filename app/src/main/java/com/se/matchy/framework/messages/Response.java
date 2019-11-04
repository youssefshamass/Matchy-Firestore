package com.se.matchy.framework.messages;

/**
 * The standard class used to communicate responses between UI and view model.
 * It has three types:
 *  - Loading (1 || 0)
 *  - Error (Throwable)
 *  - Succeeded (Object)
 */
public class Response {
    //region Response Types

    public static class Loading extends Response {
        private boolean mIsLoading;

        public Loading(boolean isLoading) {
            mIsLoading = isLoading;
        }

        public boolean isLoading() {
            return mIsLoading;
        }

        public void setLoading(boolean loading) {
            mIsLoading = loading;
        }
    }

    public static class Succeed extends Response {
        private Object mData;

        public Succeed(Object data) {
            mData = data;
        }

        public Object getData() {
            return mData;
        }

        public void setData(Object data) {
            mData = data;
        }
    }

    public static class Error extends Response {
        private Throwable mThrowable;
        private String mMessage;

        public Error(Throwable throwable, String message) {
            mThrowable = throwable;
            mMessage = message;
        }

        public Error(Throwable throwable) {
            mThrowable = throwable;
            mMessage = mThrowable.getMessage();
        }

        public Throwable getThrowable() {
            return mThrowable;
        }

        public void setThrowable(Throwable throwable) {
            mThrowable = throwable;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String message) {
            mMessage = message;
        }
    }

    //endregion
}
