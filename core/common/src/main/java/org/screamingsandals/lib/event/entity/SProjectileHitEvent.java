package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SProjectileHitEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<EntityBasic> hitEntity;
    private final ImmutableObjectLink<BlockHolder> hitBlock;
    private final ImmutableObjectLink<BlockFace> hitFace;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public EntityBasic getHitEntity() {
        return hitEntity.get();
    }

    public BlockHolder getHitBlock() {
        return hitBlock.get();
    }

    public BlockFace getHitFace() {
        return hitFace.get();
    }
}
