package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SPlayerKickEvent extends CancellableAbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<Component> leaveMessage;
    private final ObjectLink<Component> kickReason;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Component getLeaveMessage() {
        return leaveMessage.get();
    }

    public void setLeaveMessage(Component leaveMessage) {
        this.leaveMessage.set(leaveMessage);
    }

    public void setLeaveMessage(ComponentLike leaveMessage) {
        this.leaveMessage.set(leaveMessage.asComponent());
    }

    public Component getKickReason() {
        return kickReason.get();
    }

    public void setKickReason(Component kickReason) {
        this.kickReason.set(kickReason);
    }

    public void setKickReason(ComponentLike kickReason) {
        this.kickReason.set(kickReason.asComponent());
    }
}
