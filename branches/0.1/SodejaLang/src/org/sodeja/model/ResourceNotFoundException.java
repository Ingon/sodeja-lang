package org.sodeja.model;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 971283538573098053L;

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}
}
