package org.screamingsandals.lib.core.lang;

import org.screamingsandals.lib.core.lang.message.Message;
import org.screamingsandals.lib.core.wrapper.sender.SenderWrapper;

public interface SLang {

    static Message mpr() {
        return m(null, true, null);
    }

    static Message mpr(String key) {
        return m(key, true, null);
    }

    static Message mpr(String key, SenderWrapper<?> sender) {
        return m(key, true, sender);
    }


    static Message m() {
        return m(null, false, null);
    }

    static Message m(String key) {
        return m(key, false, null);
    }

    static Message m(String key, SenderWrapper<?> sender) {
        return m(key, false, sender);
    }

    static Message m(String key, boolean prefix, SenderWrapper<?> sender) {
        return new Message(key, prefix, sender);
    }
}
