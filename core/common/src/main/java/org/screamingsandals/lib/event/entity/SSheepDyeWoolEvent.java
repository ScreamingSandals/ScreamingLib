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
public class SSheepDyeWoolEvent extends CancellableAbstractEvent {
    //TODO: DyeColor holder
    private final ImmutableObjectLink<EntityBasic> entity;
    private final ObjectLink<String> dyeColor;

    public EntityBasic getEntity() {
        return entity.get();
    }

    public String getDyeColor() {
        return dyeColor.get();
    }

    public void setDyeColor(String dyeColor) {
        this.dyeColor.set(dyeColor);
    }
}
