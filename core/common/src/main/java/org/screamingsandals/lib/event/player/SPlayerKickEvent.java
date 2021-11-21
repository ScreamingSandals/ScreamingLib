package org.screamingsandals.lib.event.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.event.SCancellableEvent;

public interface SPlayerKickEvent extends SCancellableEvent, SPlayerEvent {

    Component getLeaveMessage();

    void setLeaveMessage(Component leaveMessage);

    void setLeaveMessage(ComponentLike leaveMessage);

    Component getKickReason();

    void setKickReason(Component kickReason);

    void setKickReason(ComponentLike kickReason);
}
