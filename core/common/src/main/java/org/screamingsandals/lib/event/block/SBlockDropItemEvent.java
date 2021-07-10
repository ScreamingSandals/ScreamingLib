package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.annotations.LimitedVersionSupport;
import org.screamingsandals.lib.world.state.BlockStateHolder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport("Bukkit >= 1.13.2")
public class SBlockDropItemEvent extends CancellableAbstractEvent {
    private final PlayerWrapper player;
    private final BlockStateHolder blockState;
    private final List<EntityItem> items;
}
