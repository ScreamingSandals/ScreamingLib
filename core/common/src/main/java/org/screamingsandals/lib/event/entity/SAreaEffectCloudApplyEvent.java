package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SAreaEffectCloudApplyEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final Collection<EntityBasic> affectedEntities;

    public EntityBasic getEntity() {
        return entity.get();
    }
}
