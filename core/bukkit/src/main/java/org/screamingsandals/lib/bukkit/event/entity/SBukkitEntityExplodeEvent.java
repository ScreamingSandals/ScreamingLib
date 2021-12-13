package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.screamingsandals.lib.block.BlockHolder;
import org.screamingsandals.lib.block.BlockMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityExplodeEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.world.LocationHolder;
import org.screamingsandals.lib.world.LocationMapper;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityExplodeEvent implements SEntityExplodeEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityExplodeEvent event;

    // Internal cache
    private EntityBasic entity;
    private LocationHolder location;
    private Collection<BlockHolder> blocks;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public LocationHolder getLocation() {
        if (location == null) {
            location = LocationMapper.wrapLocation(event.getLocation());
        }
        return location;
    }

    @Override
    public Collection<BlockHolder> getBlocks() {
        if (blocks == null) {
            blocks = new CollectionLinkedToCollection<>(event.blockList(), o -> o.as(Block.class), BlockMapper::wrapBlock);
        }
        return blocks;
    }

    @Override
    public float getYield() {
        return event.getYield();
    }

    @Override
    public void setYield(float yield) {
        event.setYield(yield);
    }
}
