package org.screamingsandals.lib.event.player;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class SPlayerJoinEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ObjectLink<@Nullable Component> joinMessage;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    @Nullable
    public Component getJoinMessage() {
        return joinMessage.get();
    }

    public void setJoinMessage(@Nullable Component joinMessage) {
        this.joinMessage.set(joinMessage);
    }
}
