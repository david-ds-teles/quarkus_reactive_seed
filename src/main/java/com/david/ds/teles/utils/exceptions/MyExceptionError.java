package com.david.ds.teles.utils.exceptions;

import lombok.Getter;

@Getter
public class MyExceptionError extends RuntimeException {
	private static final long serialVersionUID = 408265269912771298L;
	private int status = 400;

	public MyExceptionError(String message) {
		super(message);
	}

	public MyExceptionError(String message, int status) {
		super(message);
		this.status = status;
	}
}
