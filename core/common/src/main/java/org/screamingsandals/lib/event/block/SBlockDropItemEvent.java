package org.screamingsandals.lib.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.player.PlayerWrapper;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.annotations.ide.LimitedVersionSupport;
import org.screamingsandals.lib.block.state.BlockStateHolder;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@LimitedVersionSupport("Bukkit >= 1.13.2")
public class SBlockDropItemEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<PlayerWrapper> player;
    private final ImmutableObjectLink<BlockStateHolder> blockState;
    private final Collection<EntityItem> items;

    public PlayerWrapper getPlayer() {
        return player.get();
    }

    public BlockStateHolder getBlockState() {
        return blockState.get();
    }
}
