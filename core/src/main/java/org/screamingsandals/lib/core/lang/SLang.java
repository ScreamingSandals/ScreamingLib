package org.screamingsandals.lib.core.lang;

import org.screamingsandals.lib.core.lang.message.Message;

public interface SLang {

    static Message mpr() {
        return m(null, true, null);
    }

    static Message mpr(String key) {
        return m(key, true, null);
    }

    static Message mpr(String key, Object player) {
        return m(key, true, player);
    }


    static Message m() {
        return m(null, false, null);
    }

    static Message m(String key) {
        return m(key, false, null);
    }

    static Message m(String key, Object player) {
        return m(key, false, player);
    }

    static Message m(String key, boolean prefix, Object player) {
        return new Message(key, prefix, player);
    }
}
