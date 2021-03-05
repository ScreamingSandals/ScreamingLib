package org.screamingsandals.lib.player.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SPlayerCommandPreprocessEvent extends CancellableAbstractEvent {
    private PlayerWrapper player;
    private Component message;
}
