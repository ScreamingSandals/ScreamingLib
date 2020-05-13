package org.screamingsandals.lib.lang;

import org.screamingsandals.lib.lang.message.Message;

public interface I {

    @Deprecated
    static String i18n(String key) {
        return i18n(key, null, true);
    }

    @Deprecated
    static String i18nonly(String key) {
        return i18n(key, null, false);
    }

    @Deprecated
    static String i18n(String key, boolean prefix) {
        return i18n(key, null, prefix);
    }

    @Deprecated
    static String i18n(String key, String defaultK) {
        return i18n(key, defaultK, true);
    }

    @Deprecated
    static String i18nonly(String key, String defaultK) {
        return i18n(key, defaultK, false);
    }

    @Deprecated
    static String i18n(String key, String def, boolean prefix) {
        if (prefix) {
            return Base.getGlobalStorage().translateWithPrefix(key, def);
        } else {
            return Base.getGlobalStorage().translate(key, def);
        }
    }

    static Message mpr() {
        return m(null, null, true);
    }

    static Message mpr(String key) {
        return m(key, null, true);
    }

    static Message mpr(String key, String def) {
        return m(key, def, true);
    }


    static Message m() {
        return m(null, null, false);
    }

    static Message m(String key) {
        return m(key, null, false);
    }

    static Message m(String key, boolean prefix) {
        return m(key, null, prefix);
    }

    static Message m(String key, String def) {
        return m(key, def, false);
    }

    static Message m(String key, String def, boolean prefix) {
        return new Message(key, Base.getGlobalStorage(), def, prefix);
    }

    @Deprecated
    private static String translate(String base, String defaultK) {
        return Base.getGlobalStorage().translate(base, defaultK);
    }
}
