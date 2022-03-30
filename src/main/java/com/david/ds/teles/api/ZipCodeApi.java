package com.david.ds.teles.api;

import com.david.ds.teles.framework.client.ViaCepClient;
import com.david.ds.teles.utils.exceptions.MyExceptionError;
import com.david.ds.teles.utils.i18n.AppMessages;
import java.util.concurrent.CompletionStage;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("/zip")
public class ZipCodeApi {
	private ViaCepClient cepClient;

	private AppMessages messages;

	public ZipCodeApi(@RestClient ViaCepClient cepClient, AppMessages messages) {
		this.cepClient = cepClient;
		this.messages = messages;
	}

	@GET
	@Produces("application/json")
	public CompletionStage<String> fetch(
		@HeaderParam("Accept-Language") String lang,
		@QueryParam("code") String zipCode
	) {
		if (zipCode == null) throw new MyExceptionError(messages.getMessage("zip_code_needed"));

		return cepClient.fetchCep(zipCode);
	}
}
