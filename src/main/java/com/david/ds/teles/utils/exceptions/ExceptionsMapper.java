package com.david.ds.teles.utils.exceptions;

import com.david.ds.teles.utils.i18n.AppMessages;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.quarkus.logging.Log;
import io.quarkus.qute.i18n.Localized;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

public class ExceptionsMapper {
	@Inject
	@Localized("pt-BR")
	private AppMessages messages;

	@ServerExceptionMapper
	public RestResponse<DefaultResponse> constraintViolation(ConstraintViolationException ex) {
		Log.warn("a constraint violation exception has been caught");

		List<String> violations = ex.getConstraintViolations().stream()
				.map(v -> v.getPropertyPath() + " " + v.getMessage()).collect(Collectors.toList());

		DefaultResponse response = new DefaultResponse(messages.invalid_data(), violations, Response.Status.BAD_REQUEST,
				Response.Status.BAD_REQUEST.getStatusCode());

		Log.warnf("returning %s with response: %s to invalid data: %s", response.status, messages.invalid_data(),
				violations);

		return RestResponse.status(Response.Status.BAD_REQUEST, response);
	}

	@ServerExceptionMapper
	public RestResponse<DefaultResponse> myExceptionError(MyExceptionError ex) {
		Log.warn("a MyExceptionError has been caught");

		Response.Status status = Response.Status.fromStatusCode(ex.getStatus());

		Log.warnf("returning status: %s with response: %s", status, ex.getMessage());
		return RestResponse.status(status, new DefaultResponse(ex.getMessage(), status, ex.getStatus()));
	}

	@ServerExceptionMapper
	public RestResponse<DefaultResponse> fallback(Throwable ex) {
		Log.warn("a fallback exception has been caught");
		Log.error(ex);

		DefaultResponse response = new DefaultResponse(messages.failed_with(""), Response.Status.INTERNAL_SERVER_ERROR,
				500);

		Log.warnf("returning status: %s with response: %s", response.status, messages.failed_with(""));
		return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR, response);
	}

	@Data
	@AllArgsConstructor
	@JsonInclude(Include.NON_NULL)
	private static class DefaultResponse {
		public String message;
		public List<String> details;
		public Response.Status status;
		public Integer statusCode;

		public DefaultResponse(String message, Status status, Integer statusCode) {
			this.message = message;
			this.status = status;
			this.statusCode = statusCode;
		}
	}
}
