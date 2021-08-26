package org.screamingsandals.lib.event.entity;

import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.utils.BlockFace;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;
import org.screamingsandals.lib.block.BlockHolder;

@EqualsAndHashCode(callSuper = true)
public class SExpBottleEvent extends SProjectileHitEvent {
    private final ObjectLink<Integer> exp;
    private final ObjectLink<Boolean> showEffect;

    public SExpBottleEvent(ImmutableObjectLink<EntityBasic> entity, ImmutableObjectLink<EntityBasic> hitEntity, ImmutableObjectLink<BlockHolder> hitBlock, ImmutableObjectLink<BlockFace> hitFace, ObjectLink<Integer> exp, ObjectLink<Boolean> showEffect) {
        super(entity, hitEntity, hitBlock, hitFace);
        this.exp = exp;
        this.showEffect = showEffect;
    }

    public int getExp() {
        return exp.get();
    }

    public void setExp(int exp) {
        this.exp.set(exp);
    }

    public boolean isShowEffect() {
        return showEffect.get();
    }

    public void setShowEffect(boolean showEffect) {
        this.showEffect.set(showEffect);
    }
}
