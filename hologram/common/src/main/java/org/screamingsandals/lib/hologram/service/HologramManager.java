package org.screamingsandals.lib.hologram.service;

import net.kyori.adventure.text.Component;
import org.screamingsandals.lib.hologram.HologramCreator;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

public class HologramManager {

    public void test(PlayerWrapper player) {
        final var holo = HologramCreator.textHologram(new LocationHolder())
                .newLine(Component.text("oi"))
                .newLine(Component.text("sucker"))
                .addViewer(player)
                .show();
    }
}
