package com.example.android.json;

public class BaseResponse {

    public boolean isSuccess() {
        return success;
    }

    public Error[] getErrors() {
        return errors;
    }

    protected boolean success;
    protected Error[] errors;
}
