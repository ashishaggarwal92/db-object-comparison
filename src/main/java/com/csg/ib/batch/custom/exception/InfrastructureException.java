package com.csg.ib.batch.custom.exception;

public class InfrastructureException extends RuntimeException {

	private static final long serialVersionUID = 2510446134913979440L;

	private final ErrorCode errorCode;

	public InfrastructureException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;

	}

	public InfrastructureException(String message, Exception ex, ErrorCode errorCode) {
		super(message, ex);
		this.errorCode = errorCode;

	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
