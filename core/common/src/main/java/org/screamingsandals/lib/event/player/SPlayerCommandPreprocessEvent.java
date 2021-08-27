package org.screamingsandals.lib.event.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SPlayerCommandPreprocessEvent extends CancellableAbstractEvent {
    private final ObjectLink<PlayerWrapper> player;
    private final ObjectLink<String> command;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public void setPlayer(PlayerWrapper player) {
        this.player.set(player);
    }

    public String getCommand() {
        return command.get();
    }

    public void setCommand(String command) {
        this.command.set(command);
    }
}
