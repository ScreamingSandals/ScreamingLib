package org.screamingsandals.lib.bukkit.event.block;

import lombok.*;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SSculkSensorReceiveEvent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitSculkSensorReceiveEvent implements SSculkSensorReceiveEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final BlockReceiveGameEvent event;

    // Internal cache
    private BlockHolder block;
    private EntityBasic entity;
    private boolean entityConverted;
    private NamespacedMappingKey underlyingEvent;

    @Override
    public BlockHolder getBlock() {
        if (block == null) {
            block = BlockMapper.wrapBlock(event.getBlock());
        }
        return block;
    }

    @Override
    public @Nullable EntityBasic getEntity() {
        if (!entityConverted) {
            if (event.getEntity() != null) {
                entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
            }
            entityConverted = true;
        }
        return entity;
    }

    @Override
    public NamespacedMappingKey getUnderlyingEvent() {
        if (underlyingEvent == null) {
            underlyingEvent = NamespacedMappingKey.of(event.getEvent().getKey().toString());
        }
        return underlyingEvent;
    }
}
