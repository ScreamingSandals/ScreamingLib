package org.screamingsandals.lib.sender;

import net.kyori.adventure.title.Title;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TitleableSenderMessage extends SenderMessage {
    @NotNull
    Title asTitle(@Nullable CommandSenderWrapper wrapper, @Nullable Title.Times times);

    @NotNull
    Title asTitle(@Nullable CommandSenderWrapper wrapper);

    @NotNull
    Title asTitle(@Nullable Title.Times times);

    @NotNull
    Title asTitle();
}
