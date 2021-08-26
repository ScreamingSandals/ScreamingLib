package org.screamingsandals.lib.event.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.screamingsandals.lib.entity.EntityBasic;
import org.screamingsandals.lib.event.CancellableAbstractEvent;
import org.screamingsandals.lib.item.Item;
import org.screamingsandals.lib.utils.ImmutableObjectLink;
import org.screamingsandals.lib.utils.ObjectLink;

import java.util.Collection;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Data
public class SEntityDeathEvent extends CancellableAbstractEvent {
    private final ImmutableObjectLink<EntityBasic> entity;
    private final Collection<Item> drops;
    private final ObjectLink<Integer> dropExp;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public int getDropExp() {
        return dropExp.get();
    }

    public void setDropExp(int dropExp) {
        this.dropExp.set(dropExp);
    }
}
