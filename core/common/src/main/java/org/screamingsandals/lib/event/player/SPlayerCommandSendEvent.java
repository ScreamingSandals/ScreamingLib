package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
public class SPlayerCommandSendEvent extends SPlayerEvent {
    private final ImmutableObjectLink<Collection<String>> commands;

    public SPlayerCommandSendEvent(ImmutableObjectLink<PlayerWrapper> player,
                                   ImmutableObjectLink<Collection<String>> commands) {
        super(player);
        this.commands = commands;
    }

    public Collection<String> getCommands() {
        return commands.get();
    }
}
