package org.screamingsandals.lib.hologram.event;

import org.screamingsandals.lib.hologram.Hologram;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.InteractType;
import org.screamingsandals.lib.visuals.event.VisualsTouchEvent;

public class HologramTouchEvent extends VisualsTouchEvent<Hologram> {

    public HologramTouchEvent(PlayerWrapper player, Hologram visual, InteractType interactType) {
        super(player, visual, interactType);
    }
}
