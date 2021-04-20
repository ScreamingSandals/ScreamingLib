package org.screamingsandals.lib.lang;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.lang.container.TranslationContainer;

import java.util.List;

public interface Messageable {
    boolean needsTranslation();

    /**
     *
     * @return translation keys if needsTranslation() returns true; otherwise list of translated messages
     */
    List<String> getKeys();

    Type getType();

    default Component getFallback() {
        return Component.empty();
    }

    default List<String> translateIfNeeded(TranslationContainer container) {
        if (needsTranslation()) {
            return container.translate(getKeys());
        } else {
            return getKeys();
        }
    }

    enum Type {
        ADVENTURE,
        LEGACY
    }
}
