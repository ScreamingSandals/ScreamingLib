package org.screamingsandals.lib.lang;

import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.utils.Nameable;

/**
 * Something that can have a name and also can be translated.
 */
public interface TranslatedNameable extends Nameable {

    static TranslatedNameable of(String name, Translation translation) {
        return new TranslatedNameable() {
            @Override
            public @NotNull Translation nameTranslation() {
                return translation;
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

    /**
     * Gets translation keys for this nameable.
     *
     * @return translation keys
     */
    @NotNull Translation nameTranslation();
}
