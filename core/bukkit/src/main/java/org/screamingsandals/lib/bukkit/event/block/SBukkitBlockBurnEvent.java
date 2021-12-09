package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.event.block.BlockBurnEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.event.block.SBlockBurnEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockBurnEvent implements SBlockBurnEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockBurnEvent event;

    // Internal cache
    private BlockHolder block;
    private BlockHolder ignitingBlock;
    private boolean ignitingBlockCached;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @Nullable BlockHolder getIgnitingBlock() {
        if (!ignitingBlockCached) {
            if (event.getIgnitingBlock() != null) {
                ignitingBlock = BlockMapper.wrapBlock(event.getIgnitingBlock());
            }
            ignitingBlockCached = true;
        }
        return ignitingBlock;
    }
}
