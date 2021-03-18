package org.screamingsandals.lib.sender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface SenderMessage extends ComponentLike {
    @NotNull
    Component asComponent(@Nullable CommandSenderWrapper wrapper);
}
