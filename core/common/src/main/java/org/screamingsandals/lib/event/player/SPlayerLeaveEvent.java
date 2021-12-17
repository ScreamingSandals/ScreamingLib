package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SEvent;

public interface SPlayerLeaveEvent extends SEvent, SPlayerEvent, PlatformEventWrapper {

    @Nullable
    Component getLeaveMessage();

    void setLeaveMessage(@Nullable Component leaveMessage);

    void setLeaveMessage(@Nullable ComponentLike leaveMessage);
}
