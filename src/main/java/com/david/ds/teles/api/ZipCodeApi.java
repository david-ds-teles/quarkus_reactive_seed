package com.david.ds.teles.api;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.david.ds.teles.framework.client.ViaCepClient;
import com.david.ds.teles.utils.exceptions.MyExceptionError;
import com.david.ds.teles.utils.i18n.AppMessages;

import io.quarkus.qute.i18n.Localized;
import io.quarkus.qute.i18n.MessageBundles;

@Path("/zip")
public class ZipCodeApi {

	private ViaCepClient cepClient;

	public ZipCodeApi(@RestClient ViaCepClient cepClient) {
		this.cepClient = cepClient;
	}

	@GET
	@Produces("application/json")
	public CompletionStage<String> fetch(@HeaderParam("Accept-Language") String lang,
			@QueryParam("code") String zipCode) {

		AppMessages message = MessageBundles.get(AppMessages.class, Localized.Literal.of(lang));

		if (zipCode == null)
			throw new MyExceptionError(message.zip_code_needed());

		return cepClient.fetchCep(zipCode);
	}

}
