package com.david.ds.teles.utils.i18n;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

@MessageBundle
public interface AppMessages {

	@Message("Hello {name}!")
	String hello_name(String name);

	@Message("You need to provide a valid zip code")
	String zip_code_needed();

	@Message("The provided email \"{email}\" is invalid")
	String invalid_email(String email);

	@Message("Operation you requested failed. {failure}")
	String failed_with(String failure);

	@Message("The data provided is invalid")
	String invalid_data();

	@Message("The requested resource was not updated")
	String not_updated();

	@Message("The requested resource was not found")
	String not_found();
}
