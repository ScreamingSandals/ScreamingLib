package org.screamingsandals.lib.bukkit.event.block;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.event.block.BlockReceiveGameEvent;
import org.jetbrains.annotations.Nullable;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.block.SSculkSensorReceiveEvent;
import org.screamingsandals.lib.utils.key.NamespacedMappingKey;

@Data
@EqualsAndHashCode(callSuper = true)
public class SBukkitSculkSensorReceiveEvent extends SSculkSensorReceiveEvent {
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
