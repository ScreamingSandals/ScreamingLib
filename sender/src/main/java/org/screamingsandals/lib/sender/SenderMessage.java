package org.screamingsandals.lib.sender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.utils.visual.TextEntry;

public interface SenderMessage extends ComponentLike {
    @NotNull
    Component asComponent(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    TextEntry asTextEntry(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    TextEntry asTextEntry(@NotNull String identifier, @Nullable CommandSenderWrapper wrapper);
}
