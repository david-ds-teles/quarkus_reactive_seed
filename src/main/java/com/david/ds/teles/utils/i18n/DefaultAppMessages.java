package com.david.ds.teles.utils.i18n;

import com.david.ds.teles.utils.exceptions.MyExceptionError;
import com.david.ds.teles.utils.http.HttpLangInterceptor;
import io.quarkus.qute.i18n.Localized;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * The way I found to run dynamically based on location passed to API and
 * intercepted by {@link HttpLangInterceptor}.
 *
 * @author davidteles
 *
 */
@ApplicationScoped
public class DefaultAppMessages implements AppMessages {
	private I18nMessages current;

	private Locale currentLocale;

	private Map<Locale, I18nMessages> bundles;

	private Map<String, Function<String[], String>> commands;

	@Inject
	public DefaultAppMessages(
		I18nMessages defaultMessages,
		@Localized("pt-BR") I18nMessages ptBRMessages
	) {
		this.currentLocale = Locale.getDefault();
		this.current = defaultMessages;

		this.bundles = new HashMap<>();
		this.bundles.put(Locale.getDefault(), defaultMessages);
		this.bundles.put(new Locale("pt_BR"), ptBRMessages);

		// need to register commands with the same messages methods to be executed only
		// using their respective keys.
		this.commands = new HashMap<>();
		this.commands.put("hello_name", params -> this.current.hello_name(params[0]));
		this.commands.put("zip_code_needed", params -> this.current.zip_code_needed());
		this.commands.put("invalid_email", params -> this.current.invalid_email(params[0]));
		this.commands.put("failed_with", params -> this.current.failed_with(params[0]));
		this.commands.put("invalid_data", params -> this.current.invalid_data());
		this.commands.put("not_updated", params -> this.current.not_updated());
		this.commands.put("not_found", params -> this.current.not_found());
	}

	@Override
	public String getMessage(String key, String[] params) {
		if (!this.commands.containsKey(key)) throw new MyExceptionError(
			"no message registered with key: " + key
		);

		String result = this.commands.get(key).apply(params);
		return result;
	}

	@Override
	public String getMessage(String key) {
		String result = this.getMessage(key, new String[] {});
		return result;
	}

	@Override
	public void setLocale(Locale locale) {
		if (locale == null) {
			this.current = this.bundles.get(Locale.getDefault());
			return;
		}

		if (locale.toString().toUpperCase().equals(this.currentLocale.toString().toUpperCase())) return;

		this.currentLocale = locale;

		if (this.bundles.containsKey(locale)) {
			this.current = this.bundles.get(locale);
		} else {
			this.current = this.bundles.get(Locale.getDefault());
		}
		// should work, but quarkus throw an exception
		//		this.current = MessageBundles.get(I18nMessages.class,
		//				Localized.Literal.of(this.currentLocale.getLanguage() + "-" + this.currentLocale.getCountry()));
	}
}
