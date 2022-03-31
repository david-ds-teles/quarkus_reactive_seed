package com.david.ds.teles.utils.i18n;

import java.util.Locale;

public interface AppMessages {
	public String getMessage(String key, String... params);

	public String getMessage(String key);

	public void setLocale(Locale locale);
}
