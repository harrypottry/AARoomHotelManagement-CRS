package com.aaroom.exception;

public class RestException extends RuntimeException {

    protected RestError restError;

    public RestException(RestError e) {
        this.restError = e;
    }

    public RestException(RestError e, String errorDesc) {
        RestError t = new RestError();
        t.setErrorCode(e.getErrorCode());
        t.setErrorDesc(errorDesc);
        this.restError = t;
    }

    public RestError getRestError() {
        return restError;
    }

}
