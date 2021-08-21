package org.screamingsandals.lib.event.entity;

import lombok.*;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SVehicleCreateEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;

    public EntityBasic getEntity() {
        return entity.get();
    }
}
