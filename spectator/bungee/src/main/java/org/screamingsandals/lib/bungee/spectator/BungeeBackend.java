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

        // TODO

        if (component instanceof TranslatableComponent) {
            return new BungeeTranslatableContent((TranslatableComponent) component);
        }

        if (component instanceof TextComponent) {
            return new BungeeTextComponent((TextComponent) component);
        }

        return new BungeeComponent(component);
    }
}
