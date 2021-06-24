package org.screamingsandals.lib.entity.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.world.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SExpBottleEvent extends SProjectileHitEvent {
    @Getter @Setter
    private int exp;
    @Getter @Setter
    private boolean showEffect;

    public SExpBottleEvent(EntityBasic entity, EntityBasic hitEntity, BlockHolder hitBlock, BlockFace hitFace, int exp, boolean showEffect) {
        super(entity, hitEntity, hitBlock, hitFace);
        this.exp = exp;
        this.showEffect = showEffect;
    }
}
