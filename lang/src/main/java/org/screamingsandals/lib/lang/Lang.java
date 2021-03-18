package org.screamingsandals.lib.lang;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

public class Lang {
    @Getter
    private static LangService defaultService;
    @Getter
    @Setter
    private static Component defaultPrefix = Component.empty();

    public static void initDefault(LangService defaultService) {
        if (Lang.defaultService != null) {
            throw new UnsupportedOperationException("Already initialized");
        }
        Lang.defaultService = defaultService;
    }

    public static TranslationContainer getFor(CommandSenderWrapper sender) {
        if (defaultService == null) {
            throw new UnsupportedOperationException("Not initialized yet");
        }
        return defaultService.getFor(sender);
    }
}
