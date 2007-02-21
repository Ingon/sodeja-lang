package org.sodeja.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultLocalizableResource implements LocalizableResource {

	protected String id;
	protected Map<Locale, String> i18nMap;

	public DefaultLocalizableResource() {
		this(null);
	}
	
	public DefaultLocalizableResource(String id) {
		this.id = id;
		i18nMap = new HashMap<Locale, String>();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocalizedValue(Locale locale) {
		return i18nMap.get(locale);
	}

	public void setLocalizedValue(Locale locale, String str) {
		i18nMap.put(locale, str);
	}

	public Collection<Locale> getAvailableLocales() {
		return i18nMap.keySet();
	}
}
