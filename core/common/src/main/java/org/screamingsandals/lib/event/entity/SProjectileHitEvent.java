package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.AbstractEvent;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SProjectileHitEvent extends AbstractEvent {
    private final EntityBasic entity;
    private final EntityBasic hitEntity;
    private final BlockHolder hitBlock;
    private final BlockFace hitFace;

    public SProjectileHitEvent(@NotNull final EntityBasic projectile) {
        this(projectile, null, null, null);
    }
}
