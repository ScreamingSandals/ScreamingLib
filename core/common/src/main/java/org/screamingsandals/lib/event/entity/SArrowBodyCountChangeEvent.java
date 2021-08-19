package org.screamingsandals.lib.event.entity;

import lombok.*;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
@Getter(AccessLevel.NONE)
public class SArrowBodyCountChangeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<Boolean> isReset;
    private final ImmutableObjectLink<Integer> oldAmount;
    private final ObjectLink<Integer> newAmount;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public boolean isReset() {
        return isReset.get();
    }

    public int getOldAmount() {
        return oldAmount.get();
    }

    public int getNewAmount() {
        return newAmount.get();
    }

    public void setNewAmount(int newAmount) {
        this.newAmount.set(newAmount);
    }
}
