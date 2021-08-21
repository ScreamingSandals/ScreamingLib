package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityItem;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SItemMergeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityItem> entity;
    private final ImmutableObjectLink<EntityItem> target;

    public EntityItem getEntity() {
        return entity.get();
    }

    public EntityItem getTarget() {
        return target.get();
    }
}
