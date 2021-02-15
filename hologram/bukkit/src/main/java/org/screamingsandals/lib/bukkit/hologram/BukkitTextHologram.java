package org.screamingsandals.lib.bukkit.hologram;

import org.screamingsandals.lib.hologram.AbstractTextHologram;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.world.LocationHolder;

public class BukkitTextHologram extends AbstractTextHologram {

    public BukkitTextHologram(LocationHolder location, boolean touchable) {
        super(location, touchable);
        registerEvents();
    }

    protected void registerEvents() {

    }

    @Override
    protected void onViewerAdded(PlayerWrapper player, boolean checkDistance) {

    }

    @Override
    protected void onViewerRemoved(PlayerWrapper player, boolean checkDistance) {

    }

    @Override
    protected void update(PlayerWrapper player, boolean checkDistance) {

    }

    @Override
    protected void updateForAll() {

    }
}
