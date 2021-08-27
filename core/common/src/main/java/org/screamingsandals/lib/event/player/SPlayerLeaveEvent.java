package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerLeaveEvent extends SPlayerEvent {
    private final ObjectLink<@Nullable Component> leaveMessage;

    public SPlayerLeaveEvent(ImmutableObjectLink<PlayerWrapper> player,
                             ObjectLink<@Nullable Component> leaveMessage) {
        super(player);
        this.leaveMessage = leaveMessage;
    }

    @Nullable
    public Component getLeaveMessage() {
        return leaveMessage.get();
    }

    public void setLeaveMessage(@Nullable Component leaveMessage) {
        this.leaveMessage.set(leaveMessage);
    }
}
