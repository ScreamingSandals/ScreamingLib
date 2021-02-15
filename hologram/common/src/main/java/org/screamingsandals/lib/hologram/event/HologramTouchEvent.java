package org.screamingsandals.lib.hologram.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.player.PlayerWrapper;

@EqualsAndHashCode(callSuper = false)
@Data
public class HologramTouchEvent extends AbstractEvent {
    private final PlayerWrapper player;
    private final Hologram hologram;
}
