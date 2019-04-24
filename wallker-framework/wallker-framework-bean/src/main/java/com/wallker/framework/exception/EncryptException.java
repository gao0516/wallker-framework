package com.wallker.framework.exception;

@SuppressWarnings("serial")
public class EncryptException extends Exception {

	/**
	 *
	 */
	public EncryptException() {
	}

	/**
	 * @param message
	 */
	public EncryptException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public EncryptException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EncryptException(String message, Throwable cause) {
		super(message, cause);
	}

}
