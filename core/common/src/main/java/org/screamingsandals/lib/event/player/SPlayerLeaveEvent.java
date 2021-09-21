package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerLeaveEvent extends AbstractEvent implements SPlayerEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<@Nullable Component> leaveMessage;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    @Nullable
    public Component getLeaveMessage() {
        return leaveMessage.get();
    }

    public void setLeaveMessage(@Nullable Component leaveMessage) {
        this.leaveMessage.set(leaveMessage);
    }

    public void setLeaveMessage(@Nullable ComponentLike leaveMessage) {
        this.leaveMessage.set(leaveMessage != null ? leaveMessage.asComponent() : null);
    }
}
