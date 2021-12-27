package org.screamingsandals.lib.lang;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.lang.container.TranslationContainer;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

/**
 * Access point for default values.
 */
public class Lang {
    /* Package Private, we want this customizable */
    @Getter
    static MiniMessage MINIMESSAGE = MiniMessage.miniMessage();

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

    public static void withParser(@NotNull MiniMessage miniMessage) {
        Lang.MINIMESSAGE = miniMessage;
    }
}
