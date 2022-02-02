package com.david.ds.teles.framework.client;

import java.util.concurrent.CompletionStage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/{cep}/json/")
@RegisterRestClient(configKey = "viacep-api")
public interface ViaCepClient {

	@GET
	CompletionStage<String> fetchCep(@PathParam("cep") String cep);
}
