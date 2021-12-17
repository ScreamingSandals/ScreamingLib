package org.screamingsandals.lib.bukkit.event.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.screamingsandals.lib.block.state.BlockStateHolder;
import org.screamingsandals.lib.block.state.BlockStateMapper;
import org.screamingsandals.lib.bukkit.event.BukkitCancellable;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityMapper;
import org.screamingsandals.lib.event.entity.SEntityCreatePortalEvent;
import org.screamingsandals.lib.utils.CollectionLinkedToCollection;
import org.screamingsandals.lib.utils.PortalType;

import java.util.Collection;

@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class SBukkitEntityCreatePortalEvent implements SEntityCreatePortalEvent, BukkitCancellable {
    @Getter
    @EqualsAndHashCode.Include
    @ToString.Include
    private final EntityCreatePortalEvent event;

    // Internal cache
    private EntityBasic entity;
    private Collection<BlockStateHolder> blocks;
    private PortalType portalType;

    @Override
    public EntityBasic getEntity() {
        if (entity == null) {
            entity = EntityMapper.wrapEntity(event.getEntity()).orElseThrow();
        }
        return entity;
    }

    @Override
    public Collection<BlockStateHolder> getBlocks() {
        if (blocks == null) {
            blocks = new CollectionLinkedToCollection<>(
                    event.getBlocks(),
                    blockStateHolder -> blockStateHolder.as(BlockState.class),
                    blockState -> BlockStateMapper.wrapBlockState(blockState).orElseThrow()
            );
        }
        return blocks;
    }

    @Override
    public PortalType getPortalType() {
        if (portalType == null) {
            portalType = PortalType.valueOf(event.getPortalType().name().toUpperCase());
        }
        return portalType;
    }
}
