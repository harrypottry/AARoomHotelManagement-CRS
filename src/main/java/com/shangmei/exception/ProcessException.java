package com.shangmei.exception;

/**
 * author zhanggt
 * 
 */
public class ProcessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7886879810834395424L;

	public static final int CODE_UNKNOWN_ERROR = -1;

	protected int code;

	public ProcessException(int code) {
		this.code = code;
	}

	public ProcessException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ProcessException(String message, Throwable e) {
		super(message, e);
	}

	public ProcessException(int code, String message, Throwable e) {
		super(message, e);
		this.code = code;
	}

	public ProcessException(int code, Throwable e) {
		super(e);
		this.code = code;
	}

	public ProcessException(Throwable e) {
		super(e);
		this.code = CODE_UNKNOWN_ERROR;
	}

	public ProcessException(String message) {
		super(message);
	}

	public int getCode() {
		return code;
	}

}
