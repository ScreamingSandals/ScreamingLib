package org.screamingsandals.lib.event.entity;

import lombok.*;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SBatToggleSleepEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    @Getter(AccessLevel.NONE)
    private final ImmutableObjectLink<Boolean> awake;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public boolean isAwake() {
        return awake.get();
    }
}
