package org.screamingsandals.lib.event.player;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = true)
public class SPlayerCommandPreprocessEvent extends SPlayerCancellableEvent {
    private final ObjectLink<String> command;

    public SPlayerCommandPreprocessEvent(ImmutableObjectLink<PlayerWrapper> player,
                                         ObjectLink<String> command) {
        super(player);
        this.command = command;
    }

    public String getCommand() {
        return command.get();
    }

    public void setCommand(String command) {
        this.command.set(command);
    }
}
