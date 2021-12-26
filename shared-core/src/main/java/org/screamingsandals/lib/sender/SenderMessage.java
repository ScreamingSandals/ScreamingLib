package org.screamingsandals.lib.sender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.visual.TextEntry;

import java.util.List;

public interface SenderMessage extends ComponentLike {
    @NotNull
    Component asComponent(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    List<Component> asComponentList(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    default TextEntry asTextEntry(@Nullable CommandSenderWrapper wrapper) {
        return TextEntry.of(asComponent(wrapper));
    }

    @NotNull
    default TextEntry asTextEntry(@NotNull String identifier, @Nullable CommandSenderWrapper wrapper) {
        return TextEntry.of(identifier, asComponent(wrapper));
    }

    static SenderMessage empty() {
        return new StaticSenderMessage(Component.empty());
    }

    static SenderMessage of(Component component) {
        return new StaticSenderMessage(component);
    }

    static SenderMessage of(ComponentLike component) {
        return new StaticSenderMessage(component.asComponent());
    }
}
