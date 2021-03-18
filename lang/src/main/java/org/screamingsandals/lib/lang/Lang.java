package org.screamingsandals.lib.lang;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.markdown.DiscordFlavor;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.sender.CommandSenderWrapper;

public class Lang {
    /* Package Private, we want this customizable */
    @Getter
    static MiniMessage MINIMESSAGE = MiniMessage.builder()
            .markdown()
            .markdownFlavor(DiscordFlavor.get())
            .build();

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
