package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerKickEvent extends SPlayerCancellableEvent {
    private final ObjectLink<Component> leaveMessage;
    private final ObjectLink<Component> kickReason;

    public SPlayerKickEvent(ImmutableObjectLink<PlayerWrapper> player,
                            ObjectLink<Component> leaveMessage,
                            ObjectLink<Component> kickReason) {
        super(player);
        this.leaveMessage = leaveMessage;
        this.kickReason = kickReason;
    }

    public Component getLeaveMessage() {
        return leaveMessage.get();
    }

    public void setLeaveMessage(Component leaveMessage) {
        this.leaveMessage.set(leaveMessage);
    }

    public Component getKickReason() {
        return kickReason.get();
    }

    public void setKickReason(Component kickReason) {
        this.kickReason.set(kickReason);
    }
}
