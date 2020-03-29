package org.screamingsandals.lib.lang;

import org.screamingsandals.lib.lang.message.Message;
import org.screamingsandals.lib.lang.storage.Storage;

public class I {
	public static String FALLBACK_LANGUAGE = "en";
	protected static Storage globalStorage;

	@Deprecated
	public static String i18n(String key) {
		return i18n(key, null, true);
	}

	@Deprecated
	public static String i18nonly(String key) {
		return i18n(key, null, false);
	}

	@Deprecated
	public static String i18n(String key, boolean prefix) {
		return i18n(key, null, prefix);
	}

	@Deprecated
	public static String i18n(String key, String defaultK) {
		return i18n(key, defaultK, true);
	}

	@Deprecated
	public static String i18nonly(String key, String defaultK) {
		return i18n(key, defaultK, false);
	}

	@Deprecated
	public static String i18n(String key, String def, boolean prefix) {
		if (prefix) {
			return globalStorage.translateWithPrefix(key, def);
		} else {
			return globalStorage.translate(key, def);
		}
	}

	public static Message mpr() {
		return m(null, null, true);
	}

	public static Message mpr(String key) {
		return m(key, null, true);
	}

	public static Message mpr(String key, String def) {
		return m(key, def, true);
	}


	public static Message m() {
		return m(null, null, false);
	}

	public static Message m(String key) {
		return m(key, null, false);
	}

	public static Message m(String key, boolean prefix) {
		return m(key, null, prefix);
	}

	public static Message m(String key, String def) {
		return m(key, def, false);
	}

	public static Message m(String key, String def, boolean prefix) {
		return new Message(key, globalStorage, def, prefix);
	}

	@Deprecated
	private static String translate(String base, String defaultK) {
		return globalStorage.translate(base, defaultK);
	}
}
