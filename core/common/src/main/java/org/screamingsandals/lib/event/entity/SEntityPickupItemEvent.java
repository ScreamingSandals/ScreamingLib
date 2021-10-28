package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityPickupItemEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<? extends EntityBasic> entity;
    private final ImmutableObjectLink<EntityItem> item;
    private final ImmutableObjectLink<Integer> remaining;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public EntityItem getItem() {
        return item.get();
    }

    public int getRemaining() {
        return remaining.get();
    }
}
