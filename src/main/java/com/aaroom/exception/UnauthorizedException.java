package com.aaroom.exception;


public class UnauthorizedException extends RestException {

	private static final long serialVersionUID = -7709575937711948055L;

    public UnauthorizedException(RestError e) {
        super(e);
    }

    public UnauthorizedException(RestError e, String errorDesc) {
        super(e, errorDesc);
    }

}
