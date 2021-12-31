package org.screamingsandals.lib.bungee.spectator;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SpectatorBackend;

public class BungeeBackend implements SpectatorBackend {
    @Override
    public Component empty() {
        // We can't use NoArgsConstructor because it's too new
        return new BungeeComponent(new TextComponent(""));
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Component wrapComponent(@Nullable BaseComponent component) {
        if (component == null) {
            return null;
        }

        // TODO:

        // NBTComponent doesn't exist for some reason

        // ScoreComponent added in a3b44aa612c629955195b4697641de1b1665a587 (Feb 2018 (1.12), but existed in MC 1.8+)
        // SelectorComponent added in the same commit

        // KeybindComponent added in fbc5f514e28dbfc3f85cb936ad95b1a979086ab6 (1.12 released on June, this is from Nov of the same year)

        if (component instanceof TranslatableComponent) {
            return new BungeeTranslatableContent((TranslatableComponent) component);
        }

        if (component instanceof TextComponent) {
            return new BungeeTextComponent((TextComponent) component);
        }

        return new BungeeComponent(component);
    }
}
