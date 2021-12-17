package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;

public interface SPlayerJoinEvent extends SEvent, SPlayerEvent, PlatformEventWrapper {

    @Nullable
    Component getJoinMessage();

    void setJoinMessage(@Nullable Component joinMessage);

    void setJoinMessage(@Nullable ComponentLike joinMessage);
}
