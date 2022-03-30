package com.david.ds.teles.utils.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import lombok.Getter;

@Getter
public class MyExceptionError extends RuntimeException {
	private static final long serialVersionUID = 408265269912771298L;

	private int status = 400;

	@JsonIgnore
	private String messageKey;

	@JsonIgnore
	private String[] messageArgs;

	public MyExceptionError(String message) {
		super(message);
	}

	public MyExceptionError(String message, int status) {
		super(message);
		this.status = status;
	}

	public MyExceptionError(String message, String messageKey, String[] messageArgs) {
		super(message);
		this.messageKey = messageKey;
		this.messageArgs = messageArgs;
	}

	public MyExceptionError(String messageKey, String[] messageArgs) {
		this.messageKey = messageKey;
		this.messageArgs = messageArgs;
	}

	@Override
	public String toString() {
		return (
			"MyExceptionError [status=" +
			status +
			", messageKey=" +
			messageKey +
			", messageArgs=" +
			Arrays.toString(messageArgs) +
			", getMessage()=" +
			getMessage() +
			"]"
		);
	}

	public MyExceptionError(int status, String messageKey, String[] messageArgs) {
		super();
		this.status = status;
		this.messageKey = messageKey;
		this.messageArgs = messageArgs;
	}
}
