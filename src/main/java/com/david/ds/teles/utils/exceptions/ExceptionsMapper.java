package com.david.ds.teles.utils.exceptions;

import com.david.ds.teles.utils.i18n.AppMessages;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.quarkus.logging.Log;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionsMapper {
	private AppMessages messages;

	public ExceptionsMapper(AppMessages messages) {
		this.messages = messages;
	}

	@ServerExceptionMapper
	public RestResponse<DefaultResponse> constraintViolation(ConstraintViolationException ex) {
		Log.warn("a constraint violation exception has been caught");

		List<String> violations = ex
			.getConstraintViolations()
			.stream()
			.map(v -> v.getPropertyPath().toString() + " " + v.getMessage())
			.collect(Collectors.toList());

		DefaultResponse response = new DefaultResponse(messages.getMessage("invalid_data"), violations);

		Log.warnf(
			"returning %s with response: %s to invalid data: %s",
			Response.Status.BAD_REQUEST,
			messages.getMessage("invalid_data"),
			violations
		);

		return RestResponse.status(Response.Status.BAD_REQUEST, response);
	}

	@ServerExceptionMapper
	public RestResponse<DefaultResponse> myExceptionError(MyExceptionError ex) {
		Log.warn("a MyExceptionError has been caught");

		Response.Status status = Response.Status.fromStatusCode(ex.getStatus());
		String message = messages.getMessage(ex.getMessageKey(), ex.getMessageArgs());
		Log.warnf("returning status: %s with response: %s", status, message);
		return RestResponse.status(status, new DefaultResponse(message));
	}

	@ServerExceptionMapper
	public RestResponse<DefaultResponse> fallback(Throwable ex) {
		Log.warn("a fallback exception has been caught");
		Log.error(ex);

		DefaultResponse response = new DefaultResponse(
			messages.getMessage("failed_with", "internal error")
		);

		Log.warnf(
			"returning status: %s with response: %s",
			Response.Status.INTERNAL_SERVER_ERROR,
			response.getMessage()
		);
		return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, response);
	}

	@Data
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	static class DefaultResponse {
		public String message;
		public List<String> details;

		public DefaultResponse(String message) {
			this.message = message;
		}
	}
}
