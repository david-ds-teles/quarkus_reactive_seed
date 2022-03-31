package com.david.ds.teles.utils.http;

import com.david.ds.teles.utils.i18n.AppMessages;
import java.util.List;
import java.util.Locale;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
public class HttpLangInterceptor implements ContainerRequestFilter {
	private AppMessages messages;

	public HttpLangInterceptor(AppMessages messages) {
		this.messages = messages;
	}

	@Override
	public void filter(ContainerRequestContext context) {
		Locale locale = null;
		String lang = context.getHeaderString("Accept-Language");
		List<Locale> locales = context.getAcceptableLanguages();

		MultivaluedMap<String, String> queryParams = context.getUriInfo().getQueryParameters();

		if (lang != null) locale = new Locale(lang);

		if (locale == null && locales != null && !locales.isEmpty()) locale = locales.get(0);

		if (
			locale == null &&
			queryParams != null &&
			!queryParams.isEmpty() &&
			queryParams.containsKey("lang")
		) locale = new Locale(queryParams.getFirst("lang"));

		messages.setLocale(locale);
	}
}
