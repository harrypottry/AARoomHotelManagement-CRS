package com.aaroom.exception;

/**
 * Created by sosoda on 2018/10/23.
 */
public class DataException extends RestException {
    public DataException(RestError e) {
        super(e);
    }

    public DataException(RestError e, String errorDesc) {
        super(e, errorDesc);
    }

}
