package org.screamingsandals.lib.lang;

import lombok.Getter;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class LangService {
    @Getter
    protected TranslationContainer fallbackContainer = TranslationContainer.EMPTY;
    protected final Map<Locale, TranslationContainer> containers = new HashMap<>();

    public TranslationContainer getFor(CommandSenderWrapper sender) {
        /* SINGLE LANGUAGE */
        if (containers.isEmpty() || sender == null) {
            return fallbackContainer;
        }

        /* MULTI LANGUAGE */
        var senderLocale = sender.getLocale();
        var container = containers.get(senderLocale);
        if (container != null) {
            return container;
        }
        return containers
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getLanguage().equals(senderLocale.getLanguage()))
                .map(Map.Entry::getValue)
                .findAny()
                .orElse(fallbackContainer);
    }
}
