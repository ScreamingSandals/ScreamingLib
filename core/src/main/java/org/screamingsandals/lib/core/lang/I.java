package org.screamingsandals.lib.lang;

import org.screamingsandals.lib.lang.message.Message;

public interface I {

    static Message mpr() {
        return m(null, true);
    }

    static Message mpr(String key) {
        return m(key, true);
    }


    static Message m() {
        return m(null, false);
    }

    static Message m(String key) {
        return m(key, false);
    }

    static Message m(String key, boolean prefix) {
        return new Message(key, Base.getGlobalContainer(), prefix);
    }
}
