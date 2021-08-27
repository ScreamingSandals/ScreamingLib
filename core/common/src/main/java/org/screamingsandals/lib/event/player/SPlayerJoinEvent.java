package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
public class SPlayerJoinEvent extends SPlayerEvent {
    private final ObjectLink<@Nullable Component> joinMessage;

    public SPlayerJoinEvent(ImmutableObjectLink<PlayerWrapper> player,
                            ObjectLink<@Nullable Component> joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    @Nullable
    public Component getJoinMessage() {
        return joinMessage.get();
    }

    public void setJoinMessage(@Nullable Component joinMessage) {
        this.joinMessage.set(joinMessage);
    }
}
