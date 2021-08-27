package org.screamingsandals.lib.event.player;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@Data
public class SPlayerCommandSendEvent extends AbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<Collection<String>> commands;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public Collection<String> getCommands() {
        return commands.get();
    }
}
