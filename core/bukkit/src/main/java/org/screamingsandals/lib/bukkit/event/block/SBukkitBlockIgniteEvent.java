package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.event.block.BlockIgniteEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SBlockIgniteEvent;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitBlockIgniteEvent implements SBlockIgniteEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockIgniteEvent event;

    // Internal cache
    private BlockHolder block;
    private IgniteCause igniteCause;
    private BlockHolder ignitingBlock;
    private boolean ignitingBlockConverted;
    private EntityBasic ignitingEntity;
    private boolean ignitingEntityConverted;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public IgniteCause getIgniteCause() {
        if (igniteCause == null) {
            igniteCause = IgniteCause.valueOf(event.getCause().toString());
        }
        return igniteCause;
    }

    @Override
    public @Nullable BlockHolder getIgnitingBlock() {
        if (!ignitingBlockConverted) {
            if (event.getIgnitingBlock() != null) {
                ignitingBlock = BlockMapper.wrapBlock(event.getIgnitingBlock());
            }
            ignitingBlockConverted = true;
        }
        return ignitingBlock;
    }

    @Override
    public @Nullable EntityBasic getIgnitingEntity() {
        if (!ignitingEntityConverted) {
            if (event.getIgnitingEntity() != null) {
                ignitingEntity = EntityMapper.wrapEntity(event.getIgnitingEntity()).orElseThrow();
            }
            ignitingEntityConverted = true;
        }
        return ignitingEntity;
    }
}
