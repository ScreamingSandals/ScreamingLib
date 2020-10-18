package org.screamingsandals.lib.core;

import org.screamingsandals.lib.core.lang.message.Message;

public interface SLang {

    static Message mpr() {
        return m(null, true, null);
    }

    static Message mpr(Object player) {
        return m(null, true, player);
    }

    static Message mpr(String key, Object player) {
        return m(key, true, player);
    }


    static Message m() {
        return m(null, false, null);
    }

    static Message m(Object player) {
        return m(null, false, player);
    }

    static Message m(String key, Object player) {
        return m(key, false, player);
    }

    static Message m(String key, boolean prefix, Object player) {
        return new Message(key, prefix, player);
    }
}
