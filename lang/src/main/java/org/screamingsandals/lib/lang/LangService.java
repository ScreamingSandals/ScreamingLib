package org.screamingsandals.lib.lang;

import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.lang.container.TranslationContainer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class LangService {
    protected final Map<Locale, TranslationContainer> containers = new HashMap<>();
    @Setter
    @Getter
    protected TranslationContainer fallbackContainer = TranslationContainer.empty();

    public Optional<TranslationContainer> getFor(Locale locale) {
        return Optional.ofNullable(containers.get(locale));
    }

    public TranslationContainer getFor(CommandSenderWrapper sender) {
        /* SINGLE LANGUAGE */
        if (containers.isEmpty() || sender == null) {
            return fallbackContainer;
        }

        /* MULTI LANGUAGE */
        final var senderLocale = getSenderLocale(sender);
        final var container = containers.get(senderLocale);
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

    protected Locale getSenderLocale(CommandSenderWrapper sender) {
        return sender.getLocale();
    }

}
