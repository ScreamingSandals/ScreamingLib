package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityEnterLoveModeEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ImmutableObjectLink<EntityBasic> humanEntity;
    private final ObjectLink<Integer> ticksInLove;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public EntityBasic getHumanEntity() {
        return humanEntity.get();
    }

    public int getTicksInLove() {
        return ticksInLove.get();
    }

    public void setTicksInLove(int ticksInLove) {
        this.ticksInLove.set(ticksInLove);
    }
}
