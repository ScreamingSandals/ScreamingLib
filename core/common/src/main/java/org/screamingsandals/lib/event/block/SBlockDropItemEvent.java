package org.screamingsandals.lib.event.block;

import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.PlatformEventWrapper;
import org.screamingsandals.lib.event.SCancellableEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@LimitedVersionSupport("Bukkit >= 1.13.2")
public interface SBlockDropItemEvent extends SCancellableEvent, PlatformEventWrapper {

    PlayerWrapper getPlayer();

    BlockStateHolder getBlockState();

    Collection<EntityItem> getItems();
}
