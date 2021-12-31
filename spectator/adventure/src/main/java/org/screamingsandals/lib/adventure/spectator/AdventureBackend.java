package org.screamingsandals.lib.adventure.spectator;

import net.kyori.adventure.text.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.spectator.Component;
import org.screamingsandals.lib.spectator.SpectatorBackend;

public class AdventureBackend implements SpectatorBackend {
    @Override
    public Component empty() {
        return new AdventureComponent(net.kyori.adventure.text.Component.empty());
    }

    @Nullable
    @Contract("null -> null; !null -> !null")
    public static Component wrapComponent(@Nullable net.kyori.adventure.text.Component component) {
        if (component == null) {
            return null;
        }

        if (component instanceof StorageNBTComponent) {
            return new AdventureStorageNBTComponent((StorageNBTComponent) component);
        }

        if (component instanceof EntityNBTComponent) {
            return new AdventureEntityNBTComponent((EntityNBTComponent) component);
        }

        if (component instanceof BlockNBTComponent) {
            return new AdventureBlockNBTComponent((BlockNBTComponent) component);
        }

        if (component instanceof TranslatableComponent) {
            return new AdventureTranslatableComponent((TranslatableComponent) component);
        }

        if (component instanceof SelectorComponent) {
            return new AdventureSelectorComponent((SelectorComponent) component);
        }

        if (component instanceof ScoreComponent) {
            return new AdventureScoreComponent((ScoreComponent) component);
        }

        if (component instanceof KeybindComponent) {
            return new AdventureKeybindComponent((KeybindComponent) component);
        }

        if (component instanceof TextComponent) {
            return new AdventureTextComponent((TextComponent) component);
        }

        return new AdventureComponent(component);
    }
}
