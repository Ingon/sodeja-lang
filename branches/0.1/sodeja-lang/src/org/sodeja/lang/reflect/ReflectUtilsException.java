package org.sodeja.lang.reflect;

public class ReflectUtilsException extends RuntimeException {

	private static final long serialVersionUID = 6133250992490704271L;
	
	public ReflectUtilsException(String message) {
		super(message);
	}

	public ReflectUtilsException(String message, Throwable cause) {
		super(message, cause);
	}
}
