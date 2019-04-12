package com.app.common.basebean;

import java.io.Serializable;

/**
 * Created by codeest on 16/10/10.
 */

public class BaseResponse<T> implements Serializable {

    private boolean error;

    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public boolean success() {
        return !error;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
